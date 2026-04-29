package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Questions", description = "Interview question management endpoints")
public class QuestionController {

    private static final Logger log =
            LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Operation(
            summary = "Get questions with filters",
            description = "Fetch questions filtered by role, difficulty, and type. " +
                    "Used by frontend onboarding screen."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questions fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    // GET /api/questions?role=backend&difficulty=medium&type=technical&limit=5
    @GetMapping
    public ResponseEntity<Map<String, Object>> getQuestions(
            @Parameter(description = "Developer role", example = "backend")
            @RequestParam(required = false) String role,

            @Parameter(description = "Question difficulty", example = "medium")
            @RequestParam(required = false) String difficulty,

            @Parameter(description = "Round type", example = "technical")
            @RequestParam(required = false) String type,

            @Parameter(description = "Max number of questions", example = "5")
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

    @Operation(
            summary = "Get question by ID",
            description = "Fetch a single question by its UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question found"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    // GET /api/questions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@Parameter(description = "Question UUID", example = "uuid-here") @PathVariable String id){

        log.info("Fetching question: {}", id);

        Optional<Question> question = questionService.getQuestionById(id);

        if(question.isEmpty()){
            throw ResourceNotFoundException.question(id);
        }
        return ResponseEntity.ok(question.get());
    }

}
