package com.prepai.prepai_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionStartRequest {

    @NotBlank(message = "UserId is required")
    private String userId;

    @NotBlank(message = "role is required")
    private String role;

    @NotBlank(message = "difficulty is required")
    private String difficulty;

    @NotBlank(message = "roundtype is required")
    private String roundType;

    private String resumeUrl; //Optional

    private Object resumeData; //Parsed resume from frontend
}
