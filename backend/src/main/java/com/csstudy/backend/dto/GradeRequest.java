package com.csstudy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeRequest {

    @NotBlank(message = "답변을 입력해주세요.")
    private String userAnswer;
}
