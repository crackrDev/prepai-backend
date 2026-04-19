package com.prepai.prepai_backend.service;

import com.prepai.prepai_backend.dto.SessionStartRequest;
import com.prepai.prepai_backend.dto.SessionStartResponse;
import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.repository.QuestionRepository;
import com.prepai.prepai_backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public SessionStartResponse startSession(SessionStartRequest request){

        //create and save session
        Session session = new Session();
        session.setUserId(request.getUserId());
        session.setRole(request.getRole());
        session.setDifficulty(request.getDifficulty());
        session.setRoundType(request.getRoundtype());
        session.setResumeUrl(request.getResumeUrl());
        session.setStatus("active");

        Session saved = sessionRepository.save(session);

        //Fetch Relevant question for this session
        List<Question> questions = questionRepository
                .findByRoleAndDifficultyAndType(request.getRole(), request.getDifficulty(), request.getRoundtype());

        //Get just the IDs — frontend will use
        List<String> questionIds = questions.stream()
                .limit(5)
                .map(Question::getId)
                .collect(Collectors.toList());

        //3.Build response
        SessionStartResponse response = new SessionStartResponse();
        response.setSessionId(saved.getId());
        response.setStartedAt(saved.getStartedAt());
        response.setQuestionIds(questionIds);

        return response;

    }
    public Session getSessionById(String id){
        return sessionRepository.findById(id).orElse(null);
    }
}
