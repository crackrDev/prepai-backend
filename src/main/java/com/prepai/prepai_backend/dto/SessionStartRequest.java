package com.prepai.prepai_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SessionStartRequest {

    @NotBlank(message = "UserId is required")
    private String userId;

    @NotBlank(message = "role is required")
    @Pattern(
            regexp = "frontend|backend|fullstack|devops|data",
            message = "role must be one of: frontend, backend, fullstack, devops, data"
    )
    private String role;

    @NotBlank(message = "difficulty is required")
    @Pattern(
            regexp = "easy|medium|hard",
            message = "difficulty must be: easy, medium, or hard"
    )
    private String difficulty;

    @NotBlank(message = "roundtype is required")
    @Pattern(
            regexp = "technical|hr",
            message = "roundType must be: technical or hr"
    )
    private String roundType;

    private String resumeUrl; //Optional

    private Object resumeData; //Parsed resume from frontend
}
