package com.csstudy.backend.repository;

import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategorySlug(String slug);

    List<Question> findByDifficulty(Difficulty difficulty);

    @Query("SELECT q FROM Question q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> searchByTitleOrContent(@Param("keyword") String keyword);

    long countByStudyCountGreaterThan(int count);

    long countByCategoryId(Long categoryId);

    long countByDifficulty(Difficulty difficulty);

    @Query("SELECT q FROM Question q ORDER BY q.studyCount ASC, q.lastStudiedAt ASC NULLS FIRST")
    List<Question> findLeastStudied(Pageable pageable);

    @Query("SELECT COUNT(q) FROM Question q WHERE q.lastStudiedAt >= :since")
    long countStudiedSince(@Param("since") LocalDateTime since);

    @Query("SELECT q.category.name, COUNT(q) FROM Question q WHERE q.studyCount > 0 GROUP BY q.category.name ORDER BY COUNT(q) DESC")
    List<Object[]> countStudiedByCategory();
}
