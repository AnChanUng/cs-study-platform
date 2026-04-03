package com.csstudy.backend.dto;

import com.csstudy.backend.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    private String answer;

    @NotNull(message = "Category slug is required")
    private String categorySlug;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    private String tags;
}
