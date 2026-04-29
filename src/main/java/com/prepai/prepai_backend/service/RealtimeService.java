package com.prepai.prepai_backend.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepai.prepai_backend.dto.RealtimeTokenResponse;
import com.prepai.prepai_backend.exception.BadRequestException;
import com.prepai.prepai_backend.exception.ResourceNotFoundException;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class RealtimeService {

    private static final Logger log =  LoggerFactory.getLogger(RealtimeService.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RealtimeTokenResponse generateToken(String sessionId){

        //1.Fetch session from MySQL
        Session session = sessionRepository.findById(sessionId).orElseThrow(()-> ResourceNotFoundException.session(sessionId));

        log.info("Generating realtime token for sessionId: {}", sessionId);

        //2.Build system prompt from session data
        String systemPrompt = buildSystemPrompt(session);

        //3.call OpenAI Realtime Sessions API
        String clientSecret = callOpenAiRealtimeApi(systemPrompt);

        //4.Return token to frontend
        RealtimeTokenResponse response = new RealtimeTokenResponse();
        response.setToken(clientSecret);
        return response;
    }
    //Build System Prompt
    private String buildSystemPrompt(Session session){
        return "You are a professional technical interviewer.\n" +
                "Candidate Name: " + nullSafe(session.getCandidateName()) + "\n" +
                "Role: " + nullSafe(session.getRole()) + "\n" +
                "Round: " + nullSafe(session.getRoundType()) + "\n" +
                "Difficulty: " + nullSafe(session.getDifficulty()) + "\n" +
                "Company: " + nullSafe(session.getCompany()) + "\n" +
                "Resume: " + nullSafe(session.getResumeText()) + "\n" +
                "Ask only 2 questions based on the resume and round type.";
    }
    private String nullSafe(String value){
        return value != null ? value : "Not provided";
    }
    //CALL OPENAI REALTIME API
    private String callOpenAiRealtimeApi(String systemPrompt){
        try{
            // Build request body
            Map<String, Object> requestBody = Map.of("model", "gpt-4o-realtime-preview-2024-10-01",
                    "modalities", List.of("audio", "text"),
                    "instructions", systemPrompt);

            String requestJson = objectMapper.writeValueAsString(requestBody);

            //Build HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/realtime/sessions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            //Send Request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("OpenAI Realtime API status: {}", response.statusCode());

            //Handle non-200 response
            if(response.statusCode() != 200){
                log.error("OpenAI API error: {}", response.body());
                throw new BadRequestException( "OpenAI Realtime API failed: " + response.statusCode());
            }
            //Parse response and extract client_secret.value
            return extractClientSecret(response.body());
        }
        catch(BadRequestException e){
            throw e; // Re-throw as is
        }
        catch(Exception e){
            log.error("Realtime API call failed: {}", e.getMessage());
            throw new BadRequestException(
                    "Failed to generate realtime token: " + e.getMessage()
            );
        }
    }
    //Extract client_secret.value
    private String extractClientSecret(String responseBody){
        try{
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode clientSecret = root.path("client_secret");
            JsonNode value = clientSecret.path("value");

            if(value.isMissingNode() || value.isNull()){
                log.error("client_secret.value missing in response: {}",
                        responseBody);
                throw new BadRequestException(
                        "client_secret.value not found in OpenAI response"
                );
            }
            String token = value.asText();
            log.info("RealTime token generated successfully");
            return token;
        }
        catch(BadRequestException e){
            throw e;
        }
        catch(Exception e){
            log.error("Failed to parse OpenAI response: {}", e.getMessage());
            throw new BadRequestException(
                    "Failed to parse OpenAI realtime response"
            );
        }
    }
}
