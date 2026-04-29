package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.dto.ResumeParseRequest;
import com.prepai.prepai_backend.dto.ResumeParseResponse;
import com.prepai.prepai_backend.service.ResumeParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
@Tag(name = "Resume", description = "Resume parsing endpoints")
public class ResumeController {

    @Autowired
    private ResumeParserService resumeParserService;

    @Operation(
            summary = "Parse resume PDF",
            description = "Downloads PDF from Supabase Storage URL, " +
                    "extracts text using PDFBox, " +
                    "identifies skills, YOE and projects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resume parsed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid resume URL")
    })

    //POST /api/resume/parse
    @PostMapping("/parse")
    public ResponseEntity<ResumeParseResponse> parseResume(
            @Valid @RequestBody ResumeParseRequest request){

        ResumeParseResponse response = resumeParserService
                .parseFromUrl(request.getResumeUrl());

        return  ResponseEntity.ok(response);
    }

}
