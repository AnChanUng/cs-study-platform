package com.csstudy.backend.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsResponse {

    private long totalQuestions;
    private long studiedQuestions;
    private Map<String, Long> byCategory;
    private Map<String, Long> byDifficulty;
}
