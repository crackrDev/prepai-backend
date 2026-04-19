package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.dto.ResumeParseRequest;
import com.prepai.prepai_backend.dto.ResumeParseResponse;
import com.prepai.prepai_backend.service.ResumeParserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeParserService resumeParserService;

    //POST /api/resume/parse
    @PostMapping("/parse")
    public ResponseEntity<ResumeParseResponse> parseResume(
            @Valid @RequestBody ResumeParseRequest request){

        ResumeParseResponse response = resumeParserService
                .parseFromUrl(request.getResumeUrl());

        return  ResponseEntity.ok(response);
    }

}
