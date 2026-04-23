package com.prepai.prepai_backend.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    //Helper methods
    public static ResourceNotFoundException session(String sessionId) {
        return new ResourceNotFoundException(
                "Session not found with id: " + sessionId);

    }

    public static ResourceNotFoundException question(String questionId) {
        return new ResourceNotFoundException(
                "Question not found with id: " + questionId );

    }

    public static ResourceNotFoundException result(String sessionId) {
        return new ResourceNotFoundException(
                "Results not ready yet for session: " + sessionId );

    }

    public static ResourceNotFoundException user(String userId) {
        return new ResourceNotFoundException(
                "User not found with id: " + userId);

    }
}
