package com.csstudy.backend.controller;

import com.csstudy.backend.dto.CategoryResponse;
import com.csstudy.backend.entity.Category;
import com.csstudy.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryResponse> getBySlug(@PathVariable String slug) {
        Category category = categoryService.findBySlug(slug);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody Category category) {
        Category created = categoryService.create(category);
        return ResponseEntity.ok(CategoryResponse.from(created));
    }
}
