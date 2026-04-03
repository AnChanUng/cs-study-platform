package com.csstudy.backend.dto;

import com.csstudy.backend.entity.Answer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponse {

    private Long answerId;
    private int score;
    private String grade;
    private String feedback;
    private List<String> matchedKeywords;
    private List<String> missedKeywords;
    private String modelAnswer;
    private LocalDateTime createdAt;

    public static GradeResponse from(Answer answer, String modelAnswer) {
        return GradeResponse.builder()
                .answerId(answer.getId())
                .score(answer.getScore())
                .grade(calculateGrade(answer.getScore()))
                .feedback(answer.getFeedback())
                .matchedKeywords(parseKeywords(answer.getMatchedKeywords()))
                .missedKeywords(parseKeywords(answer.getMissedKeywords()))
                .modelAnswer(modelAnswer)
                .createdAt(answer.getCreatedAt())
                .build();
    }

    public static String calculateGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 70) return "B";
        if (score >= 50) return "C";
        if (score >= 30) return "D";
        return "F";
    }

    private static List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.asList(keywords.split(","));
    }
}
