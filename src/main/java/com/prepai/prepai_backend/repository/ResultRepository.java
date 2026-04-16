package com.prepai.prepai_backend.repository;

import com.prepai.prepai_backend.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ResultRepository extends JpaRepository<Result,String> {
    Optional<Result> findBySessionId(String sessionId);

}
