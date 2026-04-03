package com.csstudy.backend.service;

import com.csstudy.backend.dto.GradeResponse;
import com.csstudy.backend.dto.GradingStatsResponse;
import com.csstudy.backend.entity.Answer;
import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradingService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    @Transactional
    public GradeResponse gradeAnswer(Long questionId, String userAnswer) {
        Question question = questionService.findById(questionId);
        String modelAnswer = question.getAnswer();

        // 키워드 추출
        Set<String> keywords = extractKeywords(modelAnswer);

        // 키워드 매칭
        List<String> matched = new ArrayList<>();
        List<String> missed = new ArrayList<>();
        String userAnswerLower = userAnswer.toLowerCase();

        for (String keyword : keywords) {
            if (userAnswerLower.contains(keyword.toLowerCase())) {
                matched.add(keyword);
            } else {
                missed.add(keyword);
            }
        }

        // 점수 계산
        int keywordScore = calculateKeywordScore(matched.size(), keywords.size());
        int depthScore = calculateDepthScore(userAnswer);
        int structureScore = calculateStructureScore(userAnswer);

        int totalScore = Math.min(100, keywordScore + depthScore + structureScore);

        // 피드백 생성
        String feedback = generateFeedback(totalScore, matched, missed);

        // Answer 엔티티 저장
        Answer answer = Answer.builder()
                .question(question)
                .userAnswer(userAnswer)
                .score(totalScore)
                .feedback(feedback)
                .matchedKeywords(String.join(",", matched))
                .missedKeywords(String.join(",", missed))
                .build();

        answer = answerRepository.save(answer);

        return GradeResponse.from(answer, modelAnswer);
    }

    public List<GradeResponse> getAnswersForQuestion(Long questionId) {
        Question question = questionService.findById(questionId);
        List<Answer> answers = answerRepository.findByQuestionIdOrderByCreatedAtDesc(questionId);
        return answers.stream()
                .map(a -> GradeResponse.from(a, question.getAnswer()))
                .collect(Collectors.toList());
    }

    public GradingStatsResponse getStats() {
        long totalAnswered = answerRepository.count();
        Double avgScore = answerRepository.findOverallAverageScore();
        double averageScore = avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 0.0;

        Map<String, Long> gradeDistribution = new LinkedHashMap<>();
        gradeDistribution.put("A", answerRepository.countByScoreGreaterThanEqual(90));
        gradeDistribution.put("B", answerRepository.countByScoreBetween(70, 89));
        gradeDistribution.put("C", answerRepository.countByScoreBetween(50, 69));
        gradeDistribution.put("D", answerRepository.countByScoreBetween(30, 49));
        gradeDistribution.put("F", answerRepository.countByScoreLessThan(30));

        List<Integer> recentScores = answerRepository.findRecentScores();
        if (recentScores.size() > 20) {
            recentScores = recentScores.subList(0, 20);
        }

        return GradingStatsResponse.builder()
                .totalAnswered(totalAnswered)
                .averageScore(averageScore)
                .gradeDistribution(gradeDistribution)
                .recentScores(recentScores)
                .build();
    }

    // ========== 키워드 추출 ==========

    Set<String> extractKeywords(String modelAnswer) {
        if (modelAnswer == null || modelAnswer.isBlank()) {
            return Collections.emptySet();
        }

        // 코드 블록 제거 (```...``` 안의 내용)
        String cleanText = modelAnswer.replaceAll("```[\\s\\S]*?```", "");

        Set<String> keywords = new LinkedHashSet<>();

        // 1. "## 핵심 키워드" 섹션에서 키워드 추출
        keywords.addAll(extractFromKeywordSection(cleanText));

        // 2. 볼드(**키워드**)로 강조된 핵심 용어 (짧은 것만)
        keywords.addAll(extractBoldTerms(cleanText));

        // 3. 핵심 기술 용어 (영문, 자주 등장)
        keywords.addAll(extractTechTerms(cleanText));

        // 최대 20개로 제한
        if (keywords.size() > 20) {
            return keywords.stream().limit(20).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return keywords;
    }

    private Set<String> extractFromKeywordSection(String text) {
        Set<String> keywords = new LinkedHashSet<>();

        Pattern sectionPattern = Pattern.compile(
                "##\\s*핵심\\s*키워드[\\s]*\\n(.*?)(?=\\n##\\s|$)",
                Pattern.DOTALL
        );
        Matcher sectionMatcher = sectionPattern.matcher(text);

        if (sectionMatcher.find()) {
            String section = sectionMatcher.group(1);
            Pattern itemPattern = Pattern.compile("[-*]\\s+(.+)");
            Matcher itemMatcher = itemPattern.matcher(section);
            while (itemMatcher.find()) {
                String item = itemMatcher.group(1).trim();
                item = item.replaceAll("[*_`]", "").trim();
                if (!item.isEmpty() && item.length() <= 30) {
                    keywords.add(item);
                }
            }
        }

        return keywords;
    }

    private Set<String> extractBoldTerms(String text) {
        Set<String> terms = new LinkedHashSet<>();

        // **볼드** 텍스트에서 짧은 용어만 (헤더 제외)
        Pattern boldPattern = Pattern.compile("\\*\\*([^*]+?)\\*\\*");
        Matcher boldMatcher = boldPattern.matcher(text);
        while (boldMatcher.find()) {
            String term = boldMatcher.group(1).trim();
            // 숫자로 시작하는 목록 항목(1. ...) 패턴 제거, 긴 문장 제거
            if (term.length() >= 2 && term.length() <= 25 && !term.matches("^\\d+\\..*")) {
                // 괄호 안 영문 포함 허용 (예: "기본키 (Primary Key)")
                terms.add(term);
            }
        }

        return terms;
    }

    private Set<String> extractTechTerms(String text) {
        Set<String> terms = new LinkedHashSet<>();

        // 대문자로 시작하는 기술 용어 (2단어까지)
        Pattern techPattern = Pattern.compile("\\b([A-Z][a-zA-Z]+(?:\\s[A-Z][a-zA-Z]+)?)\\b");
        Matcher techMatcher = techPattern.matcher(text);

        Map<String, Integer> counts = new LinkedHashMap<>();
        Set<String> stopWords = Set.of(
                "The", "This", "That", "These", "Those", "There", "Here",
                "When", "Where", "What", "Which", "Who", "How", "Why",
                "Each", "Every", "Some", "Any", "All", "Both", "Other",
                "Also", "Just", "Only", "Even", "Still", "Yet", "Very"
        );

        while (techMatcher.find()) {
            String term = techMatcher.group(1);
            if (term.length() >= 3 && !stopWords.contains(term)) {
                counts.merge(term, 1, Integer::sum);
            }
        }

        // 2회 이상 등장하는 영문 기술 용어
        counts.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(8)
                .forEach(e -> terms.add(e.getKey()));

        return terms;
    }

    // ========== 점수 계산 ==========

    private int calculateKeywordScore(int matchedCount, int totalKeywords) {
        if (totalKeywords == 0) return 30;
        double ratio = (double) matchedCount / totalKeywords;
        return (int) Math.round(ratio * 60);
    }

    private int calculateDepthScore(String userAnswer) {
        int length = userAnswer.length();
        if (length < 50) return 0;
        if (length < 100) return 5;
        if (length < 200) return 10;
        if (length < 400) return 15;
        return 20;
    }

    private int calculateStructureScore(String userAnswer) {
        int score = 0;
        String answer = userAnswer.toLowerCase();

        if (containsAny(answer, "장점", "단점", "이점", "약점", "장단점")) score += 5;
        if (containsAny(answer, "예를 들", "예시", "예로", "예컨대")) score += 5;
        if (containsAny(answer, "반면", "비교", "차이", "대비", "반대로", "vs")) score += 5;
        if (answer.contains("1.") || answer.contains("1)") ||
                answer.contains("첫째") || answer.contains("먼저") || answer.contains("- ")) score += 5;

        return Math.min(20, score);
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) return true;
        }
        return false;
    }

    // ========== 피드백 생성 ==========

    private String generateFeedback(int score, List<String> matched, List<String> missed) {
        StringBuilder fb = new StringBuilder();

        if (score >= 90) fb.append("훌륭합니다! 핵심 개념을 정확하게 설명했습니다.");
        else if (score >= 70) fb.append("좋습니다! 대부분의 개념을 잘 설명했지만, 일부 키워드가 빠졌습니다.");
        else if (score >= 50) fb.append("기본적인 이해는 있지만, 더 깊은 설명이 필요합니다.");
        else if (score >= 30) fb.append("핵심 개념 일부를 놓치고 있습니다. 다시 학습해보세요.");
        else fb.append("많은 핵심 개념이 빠져있습니다. 해당 주제를 다시 공부해보세요.");

        if (!matched.isEmpty()) {
            fb.append(" 잘 설명한 키워드: ").append(String.join(", ", matched)).append(".");
        }

        if (!missed.isEmpty() && missed.size() <= 10) {
            fb.append(" 놓친 키워드: ").append(String.join(", ", missed)).append(".");
        }

        return fb.toString();
    }
}
