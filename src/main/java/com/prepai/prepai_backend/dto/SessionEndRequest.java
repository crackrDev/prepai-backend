package com.prepai.prepai_backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionEndRequest {
    @NotBlank(message = "endedAt is required")
    private String endedAt;

    private String codeSubmission; //optional
}
