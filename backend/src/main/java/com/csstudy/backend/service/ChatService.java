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
            하지만 CS 지식은 정��하고 깊이 있게 답변해야 해.
            답변은 간결하되 핵심을 놓치지 마.
            코드 ��시가 필요하면 짧게 포함해.
            모르는 건 솔직하게 모른다고 해.
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

    private Map<String, Object> callGemini(String userMessage, List<Map<String, String>> history) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

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
                "parts", List.of(Map.of("text", SYSTEM_PROMPT))
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
        String lower = userMessage.toLowerCase();
        List<Question> all = questionRepository.findAll();

        List<Question> matched = all.stream()
                .filter(q -> q.getTitle().toLowerCase().contains(lower)
                        || (q.getTags() != null && q.getTags().toLowerCase().contains(lower))
                        || (q.getContent() != null && q.getContent().toLowerCase().contains(lower)))
                .limit(3)
                .toList();

        if (matched.isEmpty()) {
            String[] keywords = lower.split("\\s+");
            matched = all.stream()
                    .filter(q -> Arrays.stream(keywords).anyMatch(k ->
                            q.getTitle().toLowerCase().contains(k)
                            || (q.getTags() != null && q.getTags().toLowerCase().contains(k))))
                    .limit(3)
                    .toList();
        }

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
        sb.append("더 자세한 내용은 해당 질문 페이지에서 확인���봐냥!");

        return Map.of("reply", sb.toString(), "source", "fallback");
    }
}
