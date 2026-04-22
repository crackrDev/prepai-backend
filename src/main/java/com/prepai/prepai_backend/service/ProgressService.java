package com.prepai.prepai_backend.service;


import com.prepai.prepai_backend.dto.ProgressResponse;
import com.prepai.prepai_backend.model.Result;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.repository.ResultRepository;
import com.prepai.prepai_backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProgressService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ResultRepository resultRepository;

    public ProgressResponse getUserProgress(String userId){
        //Fetch only scored session
        List<Session> scoredSessions = sessionRepository.findByUserIdAndStatus(userId, "scored");

        if(scoredSessions.isEmpty()){
            return buildEmptyProgress();
        }
        //Fetch result for each session
        List<Result> results = new ArrayList<>();
        List<ProgressResponse.SessionProgressItem> sessionItems = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for(Session session : scoredSessions){
            Optional<Result> resultOpt = resultRepository.findBySessionId(session.getId());

            resultOpt.ifPresent(result -> {results.add(result);

                //Build session progress item
                ProgressResponse.SessionProgressItem item = new ProgressResponse.SessionProgressItem();
                item.setSessionId(session.getId());
                item.setRole(session.getRole());
                item.setRoundType(session.getRoundType());
                item.setOverallScore(result.getOverallScore());
                item.setDate(session.getStartedAt() != null
                        ? session.getStartedAt().format(formatter) : "");
                sessionItems.add(item);

            });
        }
        //Build trend data - each category scores over time
        Map<String, List<Integer>> trends = buildTrends(results);

        //Calculate average score
        double avgScore = results.stream()
                .filter(r ->r.getOverallScore() != null)
                .mapToInt(Result::getOverallScore)
                .average()
                .orElse(0.0);

        //Find weakest area
        String weakestArea = findWeakestArea(results);

        //Final response
        ProgressResponse response = new ProgressResponse();
        response.setSessions(sessionItems);
        response.setTrends(trends);
        response.setWeakestArea(weakestArea);
        response.setAvgScore(Math.round(avgScore * 10.0) / 10.0);

        return response;
    }
    //Build Trends
    private Map<String, List<Integer>> buildTrends(List<Result> results){
        Map<String, List<Integer>> trends = new LinkedHashMap<>();

        List<Integer> communication = new ArrayList<>();
        List<Integer> technical = new ArrayList<>();
        List<Integer> problemSolving = new ArrayList<>();
        List<Integer> codeQuality = new ArrayList<>();
        List<Integer> confidence = new ArrayList<>();
        List<Integer> overall = new ArrayList<>();

        for (Result result : results) {
            if (result.getCommunication() != null)
                communication.add(result.getCommunication());
            if (result.getTechnical() != null)
                technical.add(result.getTechnical());
            if (result.getProblemSolving() != null)
                problemSolving.add(result.getProblemSolving());
            if (result.getCodeQuality() != null)
                codeQuality.add(result.getCodeQuality());
            if (result.getConfidence() != null)
                confidence.add(result.getConfidence());
            if (result.getOverallScore() != null)
                overall.add(result.getOverallScore());
        }

        trends.put("communication", communication);
        trends.put("technical", technical);
        trends.put("problemSolving", problemSolving);
        trends.put("codeQuality", codeQuality);
        trends.put("confidence", confidence);
        trends.put("overall", overall);

        return trends;
    }
    //Finding weakest Area
    private String findWeakestArea(List<Result> results){
        if(results.isEmpty()) return "N/A";

        //Average per category
        Map<String, Double> categoryAvgs = new LinkedHashMap<>();

        categoryAvgs.put("Communication", results.stream()
                .filter(r ->r.getCommunication() != null)
                .mapToInt(Result::getCommunication)
                .average().orElse(100));

        categoryAvgs.put("Technical", results.stream()
                .filter(r -> r.getTechnical() != null)
                .mapToInt(Result::getTechnical)
                .average().orElse(100));

        categoryAvgs.put("Problem Solving", results.stream()
                .filter(r -> r.getProblemSolving() != null)
                .mapToInt(Result::getProblemSolving)
                .average().orElse(100));

        categoryAvgs.put("Code Quality", results.stream()
                .filter(r -> r.getCodeQuality() != null)
                .mapToInt(Result::getCodeQuality)
                .average().orElse(100));

        categoryAvgs.put("Confidence", results.stream()
                .filter(r -> r.getConfidence() != null)
                .mapToInt(Result::getConfidence)
                .average().orElse(100));

        // Return category with lowest average
        return categoryAvgs.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

    }
    //Empty Progress
    private ProgressResponse buildEmptyProgress() {
        ProgressResponse response = new ProgressResponse();
        response.setSessions(new ArrayList<>());
        response.setTrends(new HashMap<>());
        response.setWeakestArea("N/A");
        response.setAvgScore(0.0);
        return response;
    }

    }

