package com.prepai.prepai_backend.service;


import com.prepai.prepai_backend.dto.MessageRequest;
import com.prepai.prepai_backend.dto.MessageResponse;
import com.prepai.prepai_backend.model.Message;
import com.prepai.prepai_backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public MessageResponse saveMessage(String sessionId, MessageRequest request){

        //1.Build message entity
        Message message = new Message();
        message.setSessionId(sessionId);
        message.setFromRole(request.getFrom());
        message.setContent(request.getText());
        message.setCodeSubmission(request.getCodeSubmission());
        message.setCreatedAt(LocalDateTime.now());

        //2.Save to MySQL
        Message saved = messageRepository.save(message);

        //3.Return response
        MessageResponse response = new MessageResponse();
        response.setMessageId(saved.getId());
        response.setSaved(true);

        return response;
    }
    public List<Message> getMessagesBySessionId(String sessionId){
        return messageRepository.findBySessionId(sessionId);
    }
}
