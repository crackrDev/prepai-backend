package com.prepai.prepai_backend.service;

import com.prepai.prepai_backend.dto.*;
import com.prepai.prepai_backend.model.Message;
import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.model.Result;
import com.prepai.prepai_backend.model.Session;
import com.prepai.prepai_backend.repository.MessageRepository;
import com.prepai.prepai_backend.repository.QuestionRepository;
import com.prepai.prepai_backend.repository.ResultRepository;
import com.prepai.prepai_backend.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private ScoringService scoringService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ResultRepository resultRepository;

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
        session.setRoundType(request.getRoundType());
        session.setResumeUrl(request.getResumeUrl());
        session.setStatus("active");

        Session saved = sessionRepository.save(session);

        //Fetch Relevant question for this session
        List<Question> questions = questionRepository
                .findByRoleAndDifficultyAndType(request.getRole(), request.getDifficulty(), request.getRoundType());

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
    //To end a session (new method)
    public SessionEndResponse endSession(String sessionId, SessionEndRequest request){
        //1.Find session
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (session == null){
            return null;
        }
        //2.Update session status
        session.setStatus("completed");
        session.setEndedAt(LocalDateTime.now());
        sessionRepository.save(session);
        //Trigger scoring automatically, so it will be async.Frontend don't wait for that it will happen in background
        scoringService.scoreSessionAsync(sessionId);

        //3.Return Response
        SessionEndResponse response = new SessionEndResponse();
        response.setSessionId(sessionId);
        response.setScoringStatus("processing");

        return response;
    }
    //Get session with all messages
    public SessionDetailResponse getSessionDetail(String sessionId){
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if(session == null) return null;

        List<Message> messages = messageRepository.findBySessionId(sessionId);

        SessionDetailResponse response = new SessionDetailResponse();
        response.setSession(session);
        response.setMessages(messages);
        response.setStatus(session.getStatus());
        return response;
    }
    //Get all sessions for a user with scores
    public List<UserSessionResponse> getUserSessions(String userId){

        List<Session> sessions = sessionRepository.findByUserId(userId);
        return sessions.stream().map(session ->{
            UserSessionResponse res = new UserSessionResponse();
            res.setId(session.getId());
            res.setRole(session.getRole());
            res.setDate(session.getStartedAt());

            //check if result exist for this session
            Optional<Result> result = resultRepository.findBySessionId(session.getId());
            result.ifPresent(r -> res.setOverallScore(r.getOverallScore()));

            return res;

        }).collect(Collectors.toList());
    }

}
