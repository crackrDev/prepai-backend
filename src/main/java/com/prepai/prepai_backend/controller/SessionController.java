package com.prepai.prepai_backend.controller;

import com.prepai.prepai_backend.dto.*;
import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.service.MessageService;
import com.prepai.prepai_backend.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Sessions", description = "Interview session management endpoints")
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private SessionService sessionService;

    @Operation(
            summary = "Start a new interview session",
            description = "Creates a new session and returns matching question IDs " +
                    "based on role, difficulty and round type."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session started successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })

    //POST  /api/sessions/start
    @PostMapping("/start")
    public ResponseEntity<?> startSession(@Valid @RequestBody SessionStartRequest request){

        log.info("Starting session for userId: {} role: {}",
                request.getUserId(), request.getRole());

        SessionStartResponse response = sessionService.startSession(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get session with messages",
            description = "Returns full session details with all messages."
    )
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

    @Operation(
            summary = "Save a message in session",
            description = "Saves AI or user message during the interview. " +
                    "Optionally includes code submission for technical rounds."
    )
    //POST /api/sessions/{id}/message
    @PostMapping("/{id}/message")
    public ResponseEntity<?> saveMessage(@PathVariable String id, @Valid @RequestBody MessageRequest request){

        log.info("Saving message for sessionId: {} from: {}",
                id, request.getFrom());

        MessageResponse response = messageService.saveMessage(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "End interview session",
            description = "Marks session as completed and triggers async GPT-4 scoring."
    )
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

    @Operation(
            summary = "Get all sessions for a user",
            description = "Returns paginated list of sessions with scores."
    )
    // GET /api/sessions/user/{userId}?page=0&size=10
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSessions(@PathVariable String userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        log.info("Fetching sessions for userId: {} page: {}", userId, page);

        PageResponse<UserSessionResponse> response = sessionService.getUserSessions(userId, page, size);

        return ResponseEntity.ok(response);
    }


}
