package com.csstudy.backend.dto;

import com.csstudy.backend.entity.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private int displayOrder;
    private String iconEmoji;

    public static CategoryResponse from(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .description(c.getDescription())
                .displayOrder(c.getDisplayOrder())
                .iconEmoji(c.getIconEmoji())
                .build();
    }
}
