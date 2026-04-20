package com.prepai.prepai_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSessionResponse {
    private String id;
    private String role;
    private LocalDateTime date;
    private Integer overallScore;
}
