package com.prepai.prepai_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionStartResponse {
    private String sessionId;
    private LocalDateTime startedAt;
    private List<String> questionIds;
}
