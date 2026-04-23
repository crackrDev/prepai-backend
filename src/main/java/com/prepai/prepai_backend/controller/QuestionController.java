package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    private static final Logger log =
            LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    // GET /api/questions?role=backend&difficulty=medium&type=technical&limit=5
    @GetMapping
    public ResponseEntity<Map<String, Object>> getQuestions(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "5") int limit){

        log.info("Fetching questions - role: {} difficulty: {} type: {}",
                role, difficulty, type);

        List<Question> questions = questionService.getQuestions(role, difficulty, type);

        // Apply limit
        if(questions.size()>limit){
            questions = questions.subList(0,limit);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        response.put("total", questions.size());

        return ResponseEntity.ok(response);
    }
    // GET /api/questions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id){

        log.info("Fetching question: {}", id);

        Optional<Question> question = questionService.getQuestionById(id);

        if(question.isEmpty()){
            throw ResourceNotFoundException.question(id);
        }
        return ResponseEntity.ok(question.get());
    }

}
