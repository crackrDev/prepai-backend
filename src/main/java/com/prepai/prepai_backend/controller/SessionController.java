package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.dto.*;
import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.service.MessageService;
import com.prepai.prepai_backend.service.SessionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionService sessionService;

    //POST  /api/sessions/start
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@Valid @RequestBody SessionStartRequest request){

        log.info("Starting session for userId: {} role: {}",
                request.getUserId(), request.getRole());

        SessionStartResponse response = sessionService.startSession(request);
        return ResponseEntity.ok(response);
    }
    //GET /api/sessions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getSession(@PathVariable String id){

        log.info("Fetching session: {}", id);

      SessionDetailResponse response = sessionService.getSessionDetail(id);

        if(response == null){
          throw ResourceNotFoundException.session(id);
        }
        return ResponseEntity.ok(response);
    }
    //POST /api/sessions/{id}/message
    @PostMapping("/{id}/message")
    public ResponseEntity<?> saveMessage(@PathVariable String id, @Valid @RequestBody MessageRequest request){

        log.info("Saving message for sessionId: {} from: {}",
                id, request.getFrom());

        MessageResponse response = messageService.saveMessage(id, request);
        return ResponseEntity.ok(response);
    }
    //POST /api/sessions/{id}/end
    @PostMapping("/{id}/end")
    public ResponseEntity<?> endSession(@PathVariable String id, @RequestBody SessionEndRequest request){

        log.info("Ending session: {}", id);

        SessionEndResponse response = sessionService.endSession(id, request);

        //If null present throw exception
        if(response == null){
           throw ResourceNotFoundException.session(id);
        }
        return  ResponseEntity.ok(response);
    }
    // GET /api/sessions/user/{userId}?page=0&size=10
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSessions(@PathVariable String userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        log.info("Fetching sessions for userId: {} page: {}", userId, page);

        PageResponse<UserSessionResponse> response = sessionService.getUserSessions(userId, page, size);

        return ResponseEntity.ok(response);
    }


}
