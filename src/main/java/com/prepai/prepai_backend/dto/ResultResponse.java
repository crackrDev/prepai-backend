package com.prepai.prepai_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResultResponse {
    private ScoreResponse scores;
    private List<ScoreResponse.FeedbackPoint> feedback;
    private List<String> improvedAreas;
    private String sessionSummary;
}
