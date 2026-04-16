package com.prepai.prepai_backend.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "id", length = 36)
    private String id; // Supabase Auth UUID

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
