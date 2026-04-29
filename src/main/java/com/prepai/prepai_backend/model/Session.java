package com.prepai.prepai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
//updating Table annotation which is named as sessions.
@Table(name = "sessions", indexes = {
        @Index(name = "idx_sessions_user_id", columnList = "user_id"),
        @Index(name = "idx_sessions_status", columnList = "status")
}
)
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

    @Column(name = "candidate_name", length = 100)
    private String candidateName;

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "resume_text", columnDefinition = "TEXT")
    private String resumeText;
}
