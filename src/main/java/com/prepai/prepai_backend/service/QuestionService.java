package com.prepai.prepai_backend.service;

import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestions(String role, String difficulty, String type){
        // Filter based on what params are provided

        if(role != null && difficulty != null && type != null){
            return questionRepository.findByRoleAndDifficultyAndType(role, difficulty, type);
        } else if (role != null && difficulty != null) {
            return questionRepository.findByRoleAndDifficulty(role, difficulty);
        }
        else if (role != null){
            return questionRepository.findByRole(role);
        }
        return questionRepository.findAll();

    }
    public Optional<Question> getQuestionById(String id){
        return questionRepository.findById(id);
    }
}
