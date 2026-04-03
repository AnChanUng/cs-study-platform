package com.csstudy.backend.controller;

import com.csstudy.backend.dto.QuestionRequest;
import com.csstudy.backend.dto.QuestionResponse;
import com.csstudy.backend.entity.Question;
import com.csstudy.backend.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAll() {
        List<QuestionResponse> questions = questionService.findAll().stream()
                .map(QuestionResponse::from)
                .toList();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getById(@PathVariable Long id) {
        Question question = questionService.findById(id);
        return ResponseEntity.ok(QuestionResponse.from(question));
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> create(@Valid @RequestBody QuestionRequest request) {
        Question question = questionService.create(request);
        return ResponseEntity.ok(QuestionResponse.from(question));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody QuestionRequest request) {
        Question question = questionService.update(id, request);
        return ResponseEntity.ok(QuestionResponse.from(question));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponse>> search(@RequestParam("q") String query) {
        List<QuestionResponse> results = questionService.search(query).stream()
                .map(QuestionResponse::from)
                .toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<List<QuestionResponse>> getByCategory(@PathVariable String slug) {
        List<QuestionResponse> questions = questionService.findByCategorySlug(slug).stream()
                .map(QuestionResponse::from)
                .toList();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/{id}/study")
    public ResponseEntity<QuestionResponse> study(@PathVariable Long id) {
        Question question = questionService.incrementStudyCount(id);
        return ResponseEntity.ok(QuestionResponse.from(question));
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<QuestionResponse> bookmark(@PathVariable Long id) {
        Question question = questionService.toggleBookmark(id);
        return ResponseEntity.ok(QuestionResponse.from(question));
    }
}
