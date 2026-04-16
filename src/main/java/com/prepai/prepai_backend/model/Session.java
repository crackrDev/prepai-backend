package com.prepai.prepai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "difficulty", nullable = false, length = 10)
    private String difficulty;

    @Column(name = "round_type", nullable = false, length = 20)
    private String roundType; // technical or hr

    @Column(name = "resume_url", columnDefinition = "TEXT")
    private String resumeUrl;

    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "status", length = 20)
    private String status = "active"; // active, completed, scored
}
