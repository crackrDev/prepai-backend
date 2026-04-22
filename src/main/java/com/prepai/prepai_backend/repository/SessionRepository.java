package com.prepai.prepai_backend.repository;

import com.prepai.prepai_backend.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session,String> {
    //Basic fetch - existing one
    List<Session> findByUserId(String userId);

    //Pagination support
    Page<Session> findByUserId(String userId, Pageable pageable);

    //only scored sessions for progress
    List<Session> findByUserIdAndStatus(String userId, String status);

}
