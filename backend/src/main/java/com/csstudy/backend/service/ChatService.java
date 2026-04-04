package com.csstudy.backend.service;

import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api-key:}")
    private String apiKey;

    private static final String SYSTEM_PROMPT = """
            너는 '카오스 고양이'야. CS 면접 준비를 도와주는 귀여운 고양이 AI 조교야.
            말투는 반말이고 고양이답게 가끔 "냥", "먀" 같은 표현을 섞어.
            하지만 CS 지식은 정확하고 깊이 있게 답변해야 해.
            답변은 간결하되 핵심을 놓치지 마.
            코드 예시가 필요하면 짧게 포함해.
            모르는 건 솔직하게 모른다고 해.

            너는 아래 CS 면접 학습 자료를 기반으로 답변해야 해.
            사용자의 질문과 관련된 참고 자료가 제공되면, 그 내용을 바탕으로 정확하게 답변해.
            참고 자료가 없는 주제라도 CS 관련이면 네 지식으로 최대한 답변해줘.
            CS와 관련 없는 질문이면 "나는 CS 전문 고양이라 그건 잘 모르겠다냥~" 이라고 해.
            """;

    public Map<String, Object> chat(String userMessage, List<Map<String, String>> history) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackChat(userMessage);
        }

        try {
            return callGemini(userMessage, history);
        } catch (Exception e) {
            log.error("Gemini API error, falling back", e);
            return fallbackChat(userMessage);
        }
    }

    private List<Question> findRelevantQuestions(String userMessage) {
        String lower = userMessage.toLowerCase();
        List<Question> all = questionRepository.findAll();

        // 1차: 전체 메시지로 매칭
        List<Question> matched = all.stream()
                .filter(q -> q.getTitle().toLowerCase().contains(lower)
                        || (q.getTags() != null && q.getTags().toLowerCase().contains(lower)))
                .limit(5)
                .toList();

        if (!matched.isEmpty()) return matched;

        // 2차: 키워드 단위로 매칭
        String[] keywords = lower.split("\\s+");
        matched = all.stream()
                .filter(q -> Arrays.stream(keywords).anyMatch(k ->
                        k.length() >= 2 && (
                                q.getTitle().toLowerCase().contains(k)
                                || (q.getTags() != null && q.getTags().toLowerCase().contains(k))
                                || (q.getContent() != null && q.getContent().toLowerCase().contains(k))
                                || (q.getAnswer() != null && q.getAnswer().toLowerCase().contains(k))
                        )))
                .limit(5)
                .toList();

        return matched;
    }

    private String buildContextFromQuestions(List<Question> questions) {
        if (questions.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("\n\n[참고 자료]\n");
        for (Question q : questions) {
            sb.append("---\n");
            sb.append("제목: ").append(q.getTitle()).append("\n");
            if (q.getTags() != null && !q.getTags().isBlank()) {
                sb.append("키워드: ").append(q.getTags()).append("\n");
            }
            if (q.getContent() != null) {
                sb.append("질문: ").append(q.getContent()).append("\n");
            }
            if (q.getAnswer() != null) {
                String answer = q.getAnswer();
                if (answer.length() > 800) {
                    answer = answer.substring(0, 800) + "...";
                }
                sb.append("모범답변: ").append(answer).append("\n");
            }
        }
        return sb.toString();
    }

    private Map<String, Object> callGemini(String userMessage, List<Map<String, String>> history) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        // Find relevant CS content for context
        List<Question> relevant = findRelevantQuestions(userMessage);
        String context = buildContextFromQuestions(relevant);
        String fullSystemPrompt = SYSTEM_PROMPT + context;

        // Build contents array with history
        List<Map<String, Object>> contents = new ArrayList<>();

        if (history != null) {
            for (Map<String, String> msg : history) {
                String role = "user".equals(msg.get("role")) ? "user" : "model";
                contents.add(Map.of(
                        "role", role,
                        "parts", List.of(Map.of("text", msg.get("content")))
                ));
            }
        }

        // Add current user message
        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", userMessage))
        ));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("system_instruction", Map.of(
                "parts", List.of(Map.of("text", fullSystemPrompt))
        ));
        body.put("contents", contents);
        body.put("generationConfig", Map.of(
                "maxOutputTokens", 1024,
                "temperature", 0.7
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map responseBody = response.getBody();
        if (responseBody == null) {
            return fallbackChat(userMessage);
        }

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
        if (candidates == null || candidates.isEmpty()) {
            return fallbackChat(userMessage);
        }

        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        String reply = parts.stream()
                .map(p -> (String) p.get("text"))
                .collect(Collectors.joining());

        return Map.of("reply", reply, "source", "ai");
    }

    private Map<String, Object> fallbackChat(String userMessage) {
        List<Question> matched = findRelevantQuestions(userMessage);

        if (matched.isEmpty()) {
            return Map.of(
                    "reply", "냥... 그 질문은 아직 잘 모르겠다냥! 😿 다른 CS 키워드로 물어봐줘!",
                    "source", "fallback"
            );
        }

        StringBuilder sb = new StringBuilder();
        sb.append("냥! 관련 내용을 찾았다냥~ 🐱\n\n");
        for (Question q : matched) {
            sb.append("**").append(q.getTitle()).append("**\n");
            String answer = q.getAnswer();
            if (answer != null && answer.length() > 300) {
                answer = answer.substring(0, 300) + "...";
            }
            sb.append(answer).append("\n\n");
        }
        sb.append("더 자세한 내용은 해당 질문 페이지에서 확인해봐냥!");

        return Map.of("reply", sb.toString(), "source", "fallback");
    }
}
