package com.prepai.prepai_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prepai.prepai_backend.dto.ScoreResponse;
import com.prepai.prepai_backend.model.Message;
import com.prepai.prepai_backend.model.Result;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.repository.MessageRepository;
import com.prepai.prepai_backend.repository.ResultRepository;
import com.prepai.prepai_backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScoringService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //ASYNC -- Called Automatically when session ends

    @Async
    public void scoreSessionAsync(String sessionId){
        try{
            System.out.println("Scoring started for session: " + sessionId);

            //1. Fetch session + messages from MySQL
            Session session = sessionRepository.findById(sessionId).orElse(null);
            if(session == null) return;

            List<Message> messages = messageRepository.findBySessionId(sessionId);
            if(messages.isEmpty()){
                System.out.println("No messages found for session: " + sessionId);
                return;
            }

            //2. Build transcript string
            String transcript = buildTranscript(messages);

            //3.Check if code submission exist
            String codeSubmission = messages.stream()
                    .filter(m -> m.getCodeSubmission() != null)
                    .map(Message::getCodeSubmission)
                    .findFirst().orElse(null);

            //4.Call GPT-4
            ScoreResponse scoreResponse = callGpt4ForScoring(
                    transcript,
                    session.getRole(),
                    session.getRoundType(),
                    codeSubmission
            );
            if(scoreResponse == null){
                System.out.println("GPT-4 scoring failed for : " + sessionId);
                return;
            }
            //5. Save Result to MySQL
            saveResult(sessionId, scoreResponse, session.getRoundType());

            //6.update session status to "scored"
            session.setStatus("scored");
            sessionRepository.save(session);

            System.out.println("GPT-4 scored for session: " + sessionId);

        }
        catch (Exception e){
            System.err.println("Scoring error: " + e.getMessage());
        }
    }
    //BUILD TRANSCRIPT
    private String buildTranscript(List<Message> messages){
        return messages.stream()
                .map(m -> {
                    String role = m.getFromRole().equals("ai") ? "Interviewer" : "Candidate";
                    return role + ": " + m.getContent();
                })
                .collect(Collectors.joining("\n"));
    }
    // CALL GPT-4
    private ScoreResponse callGpt4ForScoring(
            String transcript,
            String role,
            String roundType,
            String codeSubmission){
        try{
            //Build Prompt
            String prompt = buildScoringPrompt(transcript, role, roundType, codeSubmission);

            //Build request Body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4");
            requestBody.put("max_tokens", 1000);
            requestBody.put("temperature", 0.3);
            requestBody.put("messages", List.of(
                    Map.of("role", "System", "content", "You are an expert technical interview evaluator. " +
                    "Always respond with valid JSON only. No explanations."),
                    Map.of("role", "user", "content", prompt)));

            String requestJson = objectMapper.writeValueAsString(requestBody);

            //HTTP call to OpenAI
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            //parse response
            return parseGpt4Response(response.body(), roundType);

        }
        catch(Exception e){
            System.err.println("GPT-4 API call failed: " + e.getMessage());
            return null;
        }
    }
    //Build Scoring Prompt
    private String buildScoringPrompt(String transcript, String role, String roundType, String codeSubmission){
        StringBuilder prompt = new StringBuilder();

        prompt.append("Evaluate this ").append(roundType)
                .append(" interview for a ").append(role)
                .append(" developer position.\n\n");

        prompt.append("TRANSCRIPT:\n").append(transcript).append("\n\n");

        if(codeSubmission != null && !codeSubmission.isEmpty()){
            prompt.append("CODE SUBMITTED:\n").append(codeSubmission).append("\n\n");
        }
        prompt.append("Return ONLY this JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"overall\": <0-100>,\n");
        prompt.append("  \"categories\": {\n");
        prompt.append("    \"communication\": <0-100>,\n");
        prompt.append("    \"technical\": <0-100>,\n");
        prompt.append("    \"problemSolving\": <0-100>,\n");

        if (codeSubmission != null) {
            prompt.append("    \"codeQuality\": <0-100>,\n");
        } else {
            prompt.append("    \"codeQuality\": null,\n");
        }

        prompt.append("    \"confidence\": <0-100>\n");
        prompt.append("  },\n");
        prompt.append("  \"feedback\": [\n");
        prompt.append("    {\"type\": \"positive\", \"text\": \"<strength>\"},\n");
        prompt.append("    {\"type\": \"positive\", \"text\": \"<strength>\"},\n");
        prompt.append("    {\"type\": \"improvement\", \"text\": \"<area to improve>\"},\n");
        prompt.append("    {\"type\": \"improvement\", \"text\": \"<area to improve>\"}\n");
        prompt.append("  ]\n");
        prompt.append("}");

        return prompt.toString();
    }
    //Parse GPT-4 Response
    private ScoreResponse parseGpt4Response(String responseBody, String roundType){
        try{
            // Extract content from OpenAI response
            Map<String, Object> response = objectMapper.readValue(
                    responseBody, Map.class
            );
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");

            Map<String, Object> message =  (Map<String, Object>) choices.get(0).get("message");

            String content =  (String) message.get("content");

            // Clean JSON (remove markdown if GPT adds backticks)
            content = content.replace("```json", "").replace("```", "").trim();

            //Parse to ScoreResponse
            return objectMapper.readValue(content, ScoreResponse.class);
        }
        catch(Exception e){
            System.err.println("Failed to parse GPT-4 response: " + e.getMessage());
            return getDefaultScores(roundType); //fallback
    }

}
//Save result to MySQL
private void saveResult(String sessionId, ScoreResponse score, String roundType){
        try{
            Result result = new Result();
            result.setSessionId(sessionId);
            result.setOverallScore(score.getOverall());
            result.setCommunication(score.getCategories().getCommunication());
            result.setTechnical(score.getCategories().getTechnical());
            result.setProblemSolving(score.getCategories().getProblemSolving());
            result.setConfidence(score.getCategories().getConfidence());

            //code quality only for technical rounds
            if (roundType.equals("technical")){
                result.setCodeQuality(score.getCategories().getCodeQuality());
            }

            //Save feedback as JSON string
            result.setFeedback(objectMapper.writeValueAsString(score.getFeedback()));

            resultRepository.save(result);
            System.out.println("Result saved to MySQL for session: " + sessionId);
        }
        catch(Exception e){
            System.err.println("Failed to save result: " + e.getMessage());
        }
}
//Default Scores (If gpt 4 fails)
    private ScoreResponse getDefaultScores(String roundType){
        ScoreResponse response = new ScoreResponse();
        response.setOverall(50);

        ScoreResponse.CategoryScores categories = new ScoreResponse.CategoryScores();
        categories.setCommunication(50);
        categories.setTechnical(50);
        categories.setProblemSolving(50);
        categories.setConfidence(50);
        if(roundType.equals("technical")){
            categories.setCodeQuality(50);
        }
        response.setCategories(categories);

        response.setFeedback(List.of(createFeedback("positive", "Attempted to answer the questions"), createFeedback("improvement", "Need more practice on technical concepts")));
        return response;
    }
    private ScoreResponse.FeedbackPoint createFeedback(String type, String text){
        ScoreResponse.FeedbackPoint point = new ScoreResponse.FeedbackPoint();
        point.setType(type);
        point.setText(text);
        return point;
    }
    //Manual score trigger
    public ScoreResponse scoreSession(String sessionId){
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if(session == null) return null;

        List<Message> messages = messageRepository.findBySessionId(sessionId);
        String transcript = buildTranscript(messages);

        String codeSubmission = messages.stream()
                .filter(m -> m.getCodeSubmission() !=null)
                .map(m -> m.getCodeSubmission())
                .findFirst().orElse(null);

        ScoreResponse score = callGpt4ForScoring(
                transcript,
                session.getRole(),
                session.getRoundType(),
                codeSubmission
        );
        if (score != null){
            saveResult(sessionId, score, session.getRoundType());
            session.setStatus("scored");
            sessionRepository.save(session);
        }
        return score;
    }

}
