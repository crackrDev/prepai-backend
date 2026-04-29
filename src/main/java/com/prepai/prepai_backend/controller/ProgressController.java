package com.prepai.prepai_backend.controller;


import com.prepai.prepai_backend.dto.ProgressResponse;
import com.prepai.prepai_backend.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
@Tag(name = "Progress", description = "User progress tracking endpoints")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @Operation(
            summary = "Get user progress",
            description = "Returns all scored sessions, " +
                    "per-category trend data, weakest area and average score. " +
                    "Used by frontend progress charts."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progress data returned"),
            @ApiResponse(responseCode = "400", description = "userId is required")
    })

    //GET /api/progress/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProgress(@PathVariable String userId){
        if(userId == null ||  userId.isBlank()){

            Map<String, String> error = new HashMap<>();
            error.put("error", "userId is required");
            return ResponseEntity.status(400).body(error);
        }

        ProgressResponse response = progressService.getUserProgress(userId);
        return ResponseEntity.ok(response);
    }
}
