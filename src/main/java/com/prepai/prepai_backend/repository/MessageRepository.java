package com.prepai.prepai_backend.repository;

import com.prepai.prepai_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,String> {
    List<Message> findBySessionId(String sessionId);
}
