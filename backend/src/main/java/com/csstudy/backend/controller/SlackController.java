package com.csstudy.backend.controller;

import com.csstudy.backend.service.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/slack")
@RequiredArgsConstructor
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/test-daily")
    public ResponseEntity<Map<String, String>> testDailyReminder() {
        slackService.sendDailyReminder();
        return ResponseEntity.ok(Map.of("message", "일일 알림 전송을 시도했습니다. 로그를 확인하세요."));
    }

    @PostMapping("/test-weekly")
    public ResponseEntity<Map<String, String>> testWeeklySummary() {
        slackService.sendWeeklySummary();
        return ResponseEntity.ok(Map.of("message", "주간 요약 전송을 시도했습니다. 로그를 확인하세요."));
    }
}
