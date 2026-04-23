package com.prepai.prepai_backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MessageRequest {

    @NotBlank(message = "From is required")
    @Pattern(
            regexp = "ai|user",
            message = "from must be: ai or user"
    )
    private String from; //ai or user

    @NotBlank(message = "text is required")
    private String text;

    @NotBlank(message = "timestamp is required")
    private String timestamp;

    private String codeSubmission; //optional - only for technical rounds
}
