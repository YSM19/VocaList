package com.example.vocatest.repository;

import com.example.vocatest.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
    List<QuizEntity> findAllQuizHistoryByEmail(String email);
}
