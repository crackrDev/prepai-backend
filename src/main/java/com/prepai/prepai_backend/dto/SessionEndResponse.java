package com.prepai.prepai_backend.dto;


import lombok.Data;

@Data
public class SessionEndResponse {
    private String sessionId;
    private String scoringStatus; //processing

}
