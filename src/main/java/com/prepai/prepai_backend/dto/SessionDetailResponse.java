package com.prepai.prepai_backend.dto;

import com.prepai.prepai_backend.model.Message;
import com.prepai.prepai_backend.model.Session;
import lombok.Data;

import java.util.List;

@Data
public class SessionDetailResponse {
    private Session session;
    private List<Message> messages;
    private String status;
}
