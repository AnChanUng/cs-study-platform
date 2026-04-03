package com.csstudy.backend.service;

import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackService {

    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;

    @Value("${slack.webhook-url}")
    private String webhookUrl;

    @Value("${slack.enabled}")
    private boolean enabled;

    @Value("${app.base-url}")
    private String baseUrl;

    public void sendDailyReminder() {
        if (!enabled) {
            log.info("Slack 알림이 비활성화 상태입니다.");
            return;
        }

        try {
            // Pick more candidates, then shuffle to get 3 random ones from the least-studied pool
            List<Question> candidates = questionRepository.findLeastStudied(PageRequest.of(0, 20));
            if (candidates.isEmpty()) {
                log.warn("데이터베이스에 질문이 없습니다. 일일 알림을 건너뜁니다.");
                return;
            }

            Collections.shuffle(candidates);
            List<Question> selected = candidates.stream().limit(3).collect(Collectors.toList());

            Map<String, Object> payload = buildDailyPayload(selected);
            sendMessage(webhookUrl, payload);
            log.info("Slack 일일 알림을 성공적으로 전송했습니다.");
        } catch (Exception e) {
            log.warn("Slack 일일 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendWeeklySummary() {
        if (!enabled) {
            log.info("Slack 알림이 비활성화 상태입니다.");
            return;
        }

        try {
            long totalQuestions = questionRepository.count();
            long studiedThisWeek = questionRepository.countStudiedSince(
                    LocalDateTime.now().minus(7, ChronoUnit.DAYS));
            long totalStudied = questionRepository.countByStudyCountGreaterThan(0);
            List<Object[]> categoryStats = questionRepository.countStudiedByCategory();

            Map<String, Object> payload = buildWeeklyPayload(
                    totalQuestions, studiedThisWeek, totalStudied, categoryStats);
            sendMessage(webhookUrl, payload);
            log.info("Slack 주간 요약을 성공적으로 전송했습니다.");
        } catch (Exception e) {
            log.warn("Slack 주간 요약 전송 실패: {}", e.getMessage(), e);
        }
    }

    private Map<String, Object> buildDailyPayload(List<Question> questions) {
        List<Map<String, Object>> blocks = new ArrayList<>();

        // Header
        blocks.add(Map.of(
                "type", "header",
                "text", Map.of(
                        "type", "plain_text",
                        "text", "📚 오늘의 CS 면접 질문",
                        "emoji", true
                )
        ));

        blocks.add(Map.of("type", "divider"));

        // Context - date
        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn",
                                "text", "매일 아침 9시에 보내드리는 CS 면접 대비 질문입니다. 오늘도 화이팅! 💪")
                )
        ));

        // Each question
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String categoryName = q.getCategory() != null ? q.getCategory().getName() : "미분류";
            String difficultyLabel = getDifficultyLabel(q.getDifficulty().name());
            String teaser = q.getContent() != null
                    ? q.getContent().substring(0, Math.min(q.getContent().length(), 100))
                    : "";
            if (q.getContent() != null && q.getContent().length() > 100) {
                teaser += "...";
            }

            String questionUrl = baseUrl + "/questions/" + q.getId();

            String text = String.format(
                    "*%d. %s*\n" +
                    "📂 카테고리: `%s`  |  📊 난이도: `%s`  |  📖 학습횟수: `%d회`\n\n" +
                    ">%s\n\n" +
                    "<%s|👉 문제 풀러 가기>",
                    i + 1, q.getTitle(),
                    categoryName, difficultyLabel, q.getStudyCount(),
                    teaser,
                    questionUrl
            );

            blocks.add(Map.of(
                    "type", "section",
                    "text", Map.of(
                            "type", "mrkdwn",
                            "text", text
                    )
            ));

            if (i < questions.size() - 1) {
                blocks.add(Map.of("type", "divider"));
            }
        }

        blocks.add(Map.of("type", "divider"));

        // Footer
        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn",
                                "text", "🔗 <" + baseUrl + "|CS Study Platform 바로가기>  |  꾸준한 학습이 합격의 지름길입니다!")
                )
        ));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("text", "📚 오늘의 CS 면접 질문이 도착했습니다!");
        payload.put("blocks", blocks);
        return payload;
    }

    private Map<String, Object> buildWeeklyPayload(long totalQuestions, long studiedThisWeek,
                                                     long totalStudied, List<Object[]> categoryStats) {
        List<Map<String, Object>> blocks = new ArrayList<>();

        // Header
        blocks.add(Map.of(
                "type", "header",
                "text", Map.of(
                        "type", "plain_text",
                        "text", "📊 주간 CS 학습 리포트",
                        "emoji", true
                )
        ));

        blocks.add(Map.of("type", "divider"));

        // Overall stats
        double progressPercent = totalQuestions > 0
                ? Math.round((double) totalStudied / totalQuestions * 1000.0) / 10.0
                : 0;

        String overviewText = String.format(
                "*📈 이번 주 학습 현황*\n\n" +
                "• 이번 주 학습한 질문: *%d개*\n" +
                "• 전체 학습 완료: *%d / %d개* (%.1f%%)\n" +
                "• 남은 질문: *%d개*",
                studiedThisWeek,
                totalStudied, totalQuestions, progressPercent,
                totalQuestions - totalStudied
        );

        blocks.add(Map.of(
                "type", "section",
                "text", Map.of("type", "mrkdwn", "text", overviewText)
        ));

        blocks.add(Map.of("type", "divider"));

        // Category breakdown
        if (!categoryStats.isEmpty()) {
            StringBuilder categoryText = new StringBuilder("*📂 카테고리별 학습 현황*\n\n");

            // Most studied (first)
            String mostStudied = (String) categoryStats.get(0)[0];
            long mostCount = (Long) categoryStats.get(0)[1];
            categoryText.append(String.format("🏆 가장 많이 학습한 카테고리: *%s* (%d개)\n", mostStudied, mostCount));

            // Least studied (last)
            if (categoryStats.size() > 1) {
                String leastStudied = (String) categoryStats.get(categoryStats.size() - 1)[0];
                long leastCount = (Long) categoryStats.get(categoryStats.size() - 1)[1];
                categoryText.append(String.format("⚠️ 가장 적게 학습한 카테고리: *%s* (%d개)\n", leastStudied, leastCount));
            }

            categoryText.append("\n");
            for (Object[] stat : categoryStats) {
                String name = (String) stat[0];
                long count = (Long) stat[1];
                categoryText.append(String.format("• %s: `%d개`\n", name, count));
            }

            blocks.add(Map.of(
                    "type", "section",
                    "text", Map.of("type", "mrkdwn", "text", categoryText.toString())
            ));
        }

        blocks.add(Map.of("type", "divider"));

        // Footer
        blocks.add(Map.of(
                "type", "context",
                "elements", List.of(
                        Map.of("type", "mrkdwn",
                                "text", "🔗 <" + baseUrl + "|CS Study Platform 바로가기>  |  이번 주도 수고하셨습니다! 다음 주도 파이팅! 🎯")
                )
        ));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("text", "📊 주간 CS 학습 리포트가 도착했습니다!");
        payload.put("blocks", blocks);
        return payload;
    }

    private void sendMessage(String webhookUrl, Object payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(webhookUrl, request, String.class);
    }

    private String getDifficultyLabel(String difficulty) {
        return switch (difficulty) {
            case "BASIC" -> "기초";
            case "INTERMEDIATE" -> "중급";
            case "ADVANCED" -> "고급";
            default -> difficulty;
        };
    }
}
