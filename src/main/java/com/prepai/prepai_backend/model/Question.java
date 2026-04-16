package com.prepai.prepai_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "questions")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "type", length = 20)
    private String type; // technical or hr

    @Column(name = "role", length = 50)
    private String role; // frontend, backend, fullstack, devops, data

    @Column(name = "difficulty", length = 10)
    private String difficulty; // easy, medium, hard

    @Column(name = "hint", columnDefinition = "TEXT")
    private String hint;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // store as comma-separated e.g. "array,hashmap,dp"
}
