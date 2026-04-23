package com.prepai.prepai_backend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Validation error (If @Valid fails)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation Failed");
        response.put("messages", errors);
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Resource Not Found");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            BadRequestException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //Generic Error (Unexpected)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("error", "Internal Server Error");
        response.put("message", "Something went wrong. Please try again.");
        response.put("timestamp", LocalDateTime.now().toString());

        //Log the Actual error
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }


}
