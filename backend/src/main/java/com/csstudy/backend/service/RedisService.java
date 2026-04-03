package com.csstudy.backend.service;

import com.csstudy.backend.dto.StatsResponse;
import com.csstudy.backend.entity.Category;
import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.repository.CategoryRepository;
import com.csstudy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    @Cacheable("stats")
    public StatsResponse getStats() {
        return refreshStats();
    }

    @CacheEvict(value = "stats", allEntries = true)
    public StatsResponse refreshStats() {
        long total = questionRepository.count();
        long studied = questionRepository.countByStudyCountGreaterThan(0);

        List<Category> categories = categoryRepository.findAll();
        Map<String, Long> byCategory = new HashMap<>();
        for (Category cat : categories) {
            long count = questionRepository.countByCategoryId(cat.getId());
            byCategory.put(cat.getSlug(), count);
        }

        Map<String, Long> byDifficulty = new HashMap<>();
        for (Difficulty diff : Difficulty.values()) {
            long count = questionRepository.countByDifficulty(diff);
            byDifficulty.put(diff.name(), count);
        }

        return StatsResponse.builder()
                .totalQuestions(total)
                .studiedQuestions(studied)
                .byCategory(byCategory)
                .byDifficulty(byDifficulty)
                .build();
    }
}
