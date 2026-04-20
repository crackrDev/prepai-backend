package com.prepai.prepai_backend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {

    @NotBlank(message = "From is required")
    private String from; //ai or user

    @NotBlank(message = "text is required")
    private String text;

    @NotBlank(message = "timestamp is required")
    private String timestamp;

    private String codeSubmission; //optional - only for technical rounds
}
