package com.prepai.prepai_backend.controller;


import com.prepai.prepai_backend.dto.RealtimeTokenRequest;
import com.prepai.prepai_backend.dto.RealtimeTokenResponse;
import com.prepai.prepai_backend.service.RealtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/realtime")
@CrossOrigin(origins = "*")
@Tag(name = "Realtime", description = "OpenAI Realtime voice session token endpoint")
public class RealtimeController {

    private static final Logger log =  LoggerFactory.getLogger(RealtimeController.class);

    @Autowired
    private RealtimeService realtimeService;

    @Operation(
            summary = "Generate OpenAI Realtime token",
            description = "Fetches session data from DB, builds system prompt, " +
                    "calls OpenAI Realtime Sessions API and returns " +
                    "client_secret token for frontend WebSocket connection.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Token generated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request or OpenAI API failure"),
            @ApiResponse(responseCode = "401",
                    description = "Invalid or missing JWT token"),
            @ApiResponse(responseCode = "404",
                    description = "Session not found")
    })
    @PostMapping("/token")
    public ResponseEntity<RealtimeTokenResponse> generateToken(@Valid @RequestBody RealtimeTokenRequest request){
        log.info("Realtime token request for sessionId: {}",
                request.getSessionId());

        RealtimeTokenResponse response = realtimeService.generateToken(request.getSessionId());

        return ResponseEntity.ok(response);
    }
}
