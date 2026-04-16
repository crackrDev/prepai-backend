package com.prepai.prepai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "session_id", nullable = false, unique = true, length = 36)
    private String sessionId;

    @Column(name = "overall_score")
    private Integer overallScore;

    @Column(name = "communication")
    private Integer communication;

    @Column(name = "technical")
    private Integer technical;

    @Column(name = "problem_solving")
    private Integer problemSolving;

    @Column(name = "code_quality")
    private Integer codeQuality; // null for HR rounds

    @Column(name = "confidence")
    private Integer confidence;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback; // JSON string stored as TEXT in MySQL

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
