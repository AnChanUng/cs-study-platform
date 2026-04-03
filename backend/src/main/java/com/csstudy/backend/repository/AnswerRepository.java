package com.csstudy.backend.repository;

import com.csstudy.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionIdOrderByCreatedAtDesc(Long questionId);

    @Query("SELECT AVG(a.score) FROM Answer a WHERE a.question.id = :questionId")
    Double findAverageScoreByQuestionId(@Param("questionId") Long questionId);

    @Query("SELECT AVG(a.score) FROM Answer a")
    Double findOverallAverageScore();

    long countByScoreGreaterThanEqual(int score);

    long countByScoreBetween(int minScore, int maxScore);

    long countByScoreLessThan(int score);

    @Query("SELECT a.score FROM Answer a ORDER BY a.createdAt DESC")
    List<Integer> findRecentScores();
}
