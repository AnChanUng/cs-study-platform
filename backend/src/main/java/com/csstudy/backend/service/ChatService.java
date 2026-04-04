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

    @Value("${anthropic.api-key:}")
    private String apiKey;

    public Map<String, Object> chat(String userMessage, List<Map<String, String>> history) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackChat(userMessage);
        }

        try {
            return callClaude(userMessage, history);
        } catch (Exception e) {
            log.error("Claude API error, falling back", e);
            return fallbackChat(userMessage);
        }
    }

    private Map<String, Object> callClaude(String userMessage, List<Map<String, String>> history) {
        String systemPrompt = """
                너는 '카오스 고양이'야. CS 면접 준비를 도와주는 귀여운 고양이 AI 조교야.
                말투는 반말이고 고양이답게 가끔 "냥", "먀" 같은 표현을 섞어.
                하지만 CS 지식은 정확하고 깊이 있게 답변해야 해.
                답변은 간결하되 핵심을 놓치지 마.
                코드 예시가 필요하면 짧게 포함해.
                모르는 건 솔직하게 모른다고 해.
                """;

        List<Map<String, String>> messages = new ArrayList<>();
        if (history != null) {
            messages.addAll(history);
        }
        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", "claude-haiku-4-5-20251001");
        body.put("max_tokens", 1024);
        body.put("system", systemPrompt);
        body.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.anthropic.com/v1/messages",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map responseBody = response.getBody();
        if (responseBody == null) {
            return fallbackChat(userMessage);
        }

        List<Map<String, Object>> content = (List<Map<String, Object>>) responseBody.get("content");
        String reply = content.stream()
                .filter(c -> "text".equals(c.get("type")))
                .map(c -> (String) c.get("text"))
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
        sb.append("더 자세한 내용은 해당 질문 페이지에서 확인해봐냥!");

        return Map.of("reply", sb.toString(), "source", "fallback");
    }
}
