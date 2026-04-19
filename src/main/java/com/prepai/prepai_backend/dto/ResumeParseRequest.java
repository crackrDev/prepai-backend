package com.prepai.prepai_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResumeParseRequest {
    @NotBlank(message = "resumeUrl is required")
    private String resumeUrl;
}
