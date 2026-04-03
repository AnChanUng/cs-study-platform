package com.csstudy.backend.dto;

import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.entity.Question;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long id;
    private String title;
    private String content;
    private String answer;
    private String categorySlug;
    private String categoryName;
    private Difficulty difficulty;
    private String tags;
    private boolean bookmarked;
    private int studyCount;
    private LocalDateTime lastStudiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuestionResponse from(Question q) {
        return QuestionResponse.builder()
                .id(q.getId())
                .title(q.getTitle())
                .content(q.getContent())
                .answer(q.getAnswer())
                .categorySlug(q.getCategory() != null ? q.getCategory().getSlug() : null)
                .categoryName(q.getCategory() != null ? q.getCategory().getName() : null)
                .difficulty(q.getDifficulty())
                .tags(q.getTags())
                .bookmarked(q.isBookmarked())
                .studyCount(q.getStudyCount())
                .lastStudiedAt(q.getLastStudiedAt())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }
}
