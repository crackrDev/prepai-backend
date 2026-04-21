package com.prepai.prepai_backend.dto;


import lombok.Data;

import java.util.List;

@Data
public class ScoreResponse {
    private Integer overall;
    private CategoryScores categories;
    private List<FeedbackPoint> feedback;

    @Data
    public static class CategoryScores {
        private Integer communication;
        private Integer technical;
        private Integer problemSolving;
        private Integer codeQuality; //null for hr rounds
        private Integer confidence;
    }

    @Data
    public static class FeedbackPoint {
        private String type; //positive or improvement
        private String text;
    }
}
