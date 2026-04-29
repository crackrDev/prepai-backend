package com.prepai.prepai_backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RealtimeTokenRequest {
    @NotBlank(message = "sessionId is required")
    private String sessionId;
}
