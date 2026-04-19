package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.dto.SessionStartRequest;
import com.prepai.prepai_backend.dto.SessionStartResponse;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    //POST  /api/sessions/start
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@Valid @RequestBody SessionStartRequest request){

        SessionStartResponse response = sessionService.startSession(request);
        return ResponseEntity.ok(response);
    }
    //GET /api/sessions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getSession(@PathVariable String id){

        Session session = sessionService.getSessionById(id);

        if(session == null){
            Map<String, String> error = new HashMap<>();
            error.put("error", "Session not found");
            return ResponseEntity.status(404).body(error);
        }
        return ResponseEntity.ok(session);
    }

}
