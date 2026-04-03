package com.csstudy.backend.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradingStatsResponse {

    private long totalAnswered;
    private double averageScore;
    private Map<String, Long> gradeDistribution;
    private List<Integer> recentScores;
}
