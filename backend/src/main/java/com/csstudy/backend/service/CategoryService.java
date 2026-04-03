package com.csstudy.backend.service;

import com.csstudy.backend.entity.Category;
import com.csstudy.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Category not found: " + slug));
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, Category updated) {
        Category category = findById(id);
        category.setName(updated.getName());
        category.setSlug(updated.getSlug());
        category.setDescription(updated.getDescription());
        category.setDisplayOrder(updated.getDisplayOrder());
        category.setIconEmoji(updated.getIconEmoji());
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
