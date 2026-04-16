package com.prepai.prepai_backend.repository;

import com.prepai.prepai_backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,String> {
    List<Question> findByRoleAndDifficultyAndType(String role, String difficulty, String type);
    List<Question> findByRoleAndDifficulty(String role, String difficulty);
    List<Question> findByRole(String role);
}
