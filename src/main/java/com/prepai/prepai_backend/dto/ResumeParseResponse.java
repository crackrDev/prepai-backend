package com.prepai.prepai_backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeParseResponse {

    private List<String> skills;
    private String yoe;
    private List<String> projects;
    private String rawText;
}
