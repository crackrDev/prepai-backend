package com.prepai.prepai_backend.repository;

import com.prepai.prepai_backend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session,String> {
    List<Session> findByUserId(String userId);
}
