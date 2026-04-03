package com.csstudy.backend.controller;

import com.csstudy.backend.dto.StatsResponse;
import com.csstudy.backend.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final RedisService redisService;

    @GetMapping
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(redisService.getStats());
    }
}
