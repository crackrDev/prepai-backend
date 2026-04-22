package com.prepai.prepai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
//Updating table annotation which is named as messages
@Table(name = "messages", indexes = {
        @Index(name = "idx_messages_session_id", columnList = "session_id")
}
)
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "session_id", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "from_role", nullable = false, length = 10)
    private String fromRole; // ai or user

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "code_submission", columnDefinition = "TEXT")
    private String codeSubmission;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
