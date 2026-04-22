package com.prepai.prepai_backend.controller;


import com.prepai.prepai_backend.dto.ProgressResponse;
import com.prepai.prepai_backend.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

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
