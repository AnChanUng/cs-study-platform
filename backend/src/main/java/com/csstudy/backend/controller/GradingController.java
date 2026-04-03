package com.csstudy.backend.controller;

import com.csstudy.backend.dto.GradeRequest;
import com.csstudy.backend.dto.GradeResponse;
import com.csstudy.backend.dto.GradingStatsResponse;
import com.csstudy.backend.service.GradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GradingController {

    private final GradingService gradingService;

    /**
     * 사용자의 답변을 채점합니다.
     */
    @PostMapping("/questions/{id}/grade")
    public ResponseEntity<GradeResponse> gradeAnswer(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request) {
        GradeResponse response = gradingService.gradeAnswer(id, request.getUserAnswer());
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 문제의 과거 답변 목록을 조회합니다.
     */
    @GetMapping("/questions/{id}/answers")
    public ResponseEntity<List<GradeResponse>> getAnswers(@PathVariable Long id) {
        List<GradeResponse> answers = gradingService.getAnswersForQuestion(id);
        return ResponseEntity.ok(answers);
    }

    /**
     * 전체 채점 통계를 조회합니다.
     */
    @GetMapping("/grading/stats")
    public ResponseEntity<GradingStatsResponse> getStats() {
        GradingStatsResponse stats = gradingService.getStats();
        return ResponseEntity.ok(stats);
    }
}
