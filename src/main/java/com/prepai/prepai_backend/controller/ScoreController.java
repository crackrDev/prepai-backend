package com.prepai.prepai_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepai.prepai_backend.dto.ResultResponse;
import com.prepai.prepai_backend.dto.ScoreResponse;
import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Result;
import com.prepai.prepai_backend.repository.ResultRepository;
import com.prepai.prepai_backend.service.ScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScoreController {

    private static final Logger log =
            LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    private ScoringService scoringService;

    @Autowired
    private ResultRepository resultRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // POST /api/score/{sessionId} — Manual trigger
    @PostMapping("/score/{sessionId}")
    public ResponseEntity<?> scoreSession(@PathVariable String sessionId){

        log.info("Manual score trigger for session: {}", sessionId);

        ScoreResponse score = scoringService.scoreSession(sessionId);

        if(score == null){
            throw ResourceNotFoundException.session(sessionId);
        }
        return ResponseEntity.ok(score);
    }
    //GET /api/results/{sessionId} — Frontend results page will use that
    @GetMapping("/results/{sessionId}")
    public ResponseEntity<?> getResults(@PathVariable String sessionId){

        log.info("Fetching results for session: {}", sessionId);

        Optional<Result> resultOpt = resultRepository.findBySessionId(sessionId);

        if(resultOpt.isEmpty()){

            log.info("Fetching results for session: {}", sessionId);
//            Map<String, String> error = new HashMap<>();
//            error.put("error", "Results not ready yet");
//            error.put("status", "processing");
//            return ResponseEntity.status(404).body(error);
        }
        Result result = resultOpt.get();

        //Build ScoreResponse
        ScoreResponse scores = new ScoreResponse();
        scores.setOverall(result.getOverallScore());

        ScoreResponse.CategoryScores categories  = new ScoreResponse.CategoryScores();
        categories.setCommunication(result.getCommunication());
        categories.setTechnical(result.getTechnical());
        categories.setProblemSolving(result.getProblemSolving());
        categories.setCodeQuality(result.getCodeQuality());
        categories.setConfidence(result.getConfidence());
        scores.setCategories(categories);

        //Parse feedback JSON
        List<ScoreResponse.FeedbackPoint> feedbackList = new ArrayList<>();
        try{
            feedbackList = objectMapper.readValue(
                    result.getFeedback(),
                    objectMapper.getTypeFactory().constructCollectionType(
                            List.class, ScoreResponse.FeedbackPoint.class
                    )
            );
        } catch (Exception e) {
            log.error("Feedback parse error: {}", e.getMessage());
            //            System.err.println("Feedback parse error: " + e.getMessage());
        }
        scores.setFeedback(feedbackList);

        //Build ResultResponse
        ResultResponse response = new ResultResponse();
        response.setScores(scores);
        response.setFeedback(feedbackList);
        response.setImprovedAreas(getImprovedAreas(result));
        response.setSessionSummary(buildSummary(result));

        return ResponseEntity.ok(response);
    }
    // Helper — Identify the weakest areas
    private List<String> getImprovedAreas(Result result){
        List<String> areas = new ArrayList<>();
        int threshold = 60;

        if (result.getCommunication() != null
                && result.getCommunication() < threshold)
            areas.add("Communication");
        if (result.getTechnical() != null
                && result.getTechnical() < threshold)
            areas.add("Technical Knowledge");
        if (result.getProblemSolving() != null
                && result.getProblemSolving() < threshold)
            areas.add("Problem Solving");
        if (result.getCodeQuality() != null
                && result.getCodeQuality() < threshold)
            areas.add("Code Quality");
        if (result.getConfidence() != null
                && result.getConfidence() < threshold)
            areas.add("Confidence");

        return areas;
    }

    //Building summary string
    private String buildSummary(Result result){
        int score = result.getOverallScore() != null ? result.getOverallScore() : 0;

        if(score >= 80) return "Excellent performance! You are interview ready.";
        if(score >= 60) return "Good attempt. Focus on weak areas and practice more.";
        if(score >= 40) return "Average performance. More preparation needed.";

        return "Keep practicing. Consistency is key for improvement.";
    }
}
