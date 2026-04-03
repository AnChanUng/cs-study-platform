package com.csstudy.backend.service;

import com.csstudy.backend.dto.QuestionRequest;
import com.csstudy.backend.entity.Category;
import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryService categoryService;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found: " + id));
    }

    public List<Question> findByCategorySlug(String slug) {
        return questionRepository.findByCategorySlug(slug);
    }

    public List<Question> findByDifficulty(Difficulty difficulty) {
        return questionRepository.findByDifficulty(difficulty);
    }

    public List<Question> search(String keyword) {
        return questionRepository.searchByTitleOrContent(keyword);
    }

    @Transactional
    public Question create(QuestionRequest request) {
        Category category = categoryService.findBySlug(request.getCategorySlug());
        Question question = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .answer(request.getAnswer())
                .category(category)
                .difficulty(request.getDifficulty())
                .tags(request.getTags())
                .build();
        return questionRepository.save(question);
    }

    @Transactional
    public Question update(Long id, QuestionRequest request) {
        Question question = findById(id);
        Category category = categoryService.findBySlug(request.getCategorySlug());
        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setAnswer(request.getAnswer());
        question.setCategory(category);
        question.setDifficulty(request.getDifficulty());
        question.setTags(request.getTags());
        return questionRepository.save(question);
    }

    @Transactional
    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    @Transactional
    public Question incrementStudyCount(Long id) {
        Question question = findById(id);
        question.setStudyCount(question.getStudyCount() + 1);
        question.setLastStudiedAt(LocalDateTime.now());
        return questionRepository.save(question);
    }

    @Transactional
    public Question toggleBookmark(Long id) {
        Question question = findById(id);
        question.setBookmarked(!question.isBookmarked());
        return questionRepository.save(question);
    }
}
