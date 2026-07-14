package com.cpbattle.CPBattle.repository;

import com.cpbattle.CPBattle.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = """
            SELECT * FROM question
            WHERE rating= :rating
            ORDER BY RANDOM()
            LIMIT 1
            """, nativeQuery = true)
    Question findRandomByRating(Integer rating);
}
