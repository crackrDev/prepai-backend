package com.prepai.prepai_backend.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProgressResponse {
    private List<SessionProgressItem> sessions;
    private Map<String, List<Integer>> trends; // category → [scores over time]
    private String weakestArea;
    private Double avgScore;

    @Data
    public static class SessionProgressItem {
        private String sessionId;
        private String role;
        private String date;
        private Integer overallScore;
        private String roundType;
    }
}
