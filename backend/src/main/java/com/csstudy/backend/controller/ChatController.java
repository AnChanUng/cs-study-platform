package com.csstudy.backend.controller;

import com.csstudy.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        String message = (String) request.get("message");
        List<Map<String, String>> history = (List<Map<String, String>>) request.get("history");

        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "메시지를 입력해주세요"));
        }

        Map<String, Object> result = chatService.chat(message.trim(), history);
        return ResponseEntity.ok(result);
    }
}
