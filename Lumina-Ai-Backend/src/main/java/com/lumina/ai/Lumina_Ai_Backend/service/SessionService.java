package com.lumina.ai.Lumina_Ai_Backend.service;

import com.lumina.ai.Lumina_Ai_Backend.dto.SessionResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.entity.Users;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.UserRepository;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
@Service
public class SessionService {
    
    private  final UserRepository userRepo;
    private  final SessionRepository sessionRepo;

    public SessionService(UserRepository userRepo,SessionRepository sessionRepo){
        this.userRepo=userRepo;
        this.sessionRepo=sessionRepo;
    }

    public Sessions createNewSession(String userId){
        Users user=userRepo.findById(Long.valueOf(userId))
                  .orElseThrow(()->new IllegalArgumentException("User Not Found"));
            
                 List<Sessions>activeSessions=sessionRepo.findAllByUserIdAndStatus(Long.valueOf(userId), Sessions.Status.ACTIVE);
for (Sessions session : activeSessions) {
            session.setStatus(Sessions.Status.EXPIRED);
            sessionRepo.save(session);
        }

        Sessions session=new Sessions();
        session.setUser(user);
        session.setSessionName("New Chat "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
        session.setStatus(Sessions.Status.ACTIVE);
        return sessionRepo.save(session);          
    }

    @Cacheable(value="sessions", key="#userId+ '-' + #sessionId")
@Transactional(readOnly = true)
    public Optional<List<SessionResponse>> getSessionHistoryDetails(String userId) {
        try {
            // Validate user exists
            Users user = userRepo.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

            // Fetch sessions and map to SessionResponse
            return sessionRepo.findAllByUserId(Long.valueOf(userId))
                    .map(sessions -> sessions.stream()
                            .map(session -> new SessionResponse(
                                    session.getId(),
                                    session.getSessionName(),
                                    session.getCreatedAt(),
                                    session.getStatus().name()))
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            System.err.println("Error fetching session history: " + e.getMessage());
            return Optional.empty();
        }
    }


    @Transactional(readOnly=true)
    public void updateSessionStatus(Long userId,Long sessionId, Sessions.Status status){
        Sessions session=sessionRepo.findByIdAndUserId(sessionId,userId).orElseThrow(()->new IllegalArgumentException("No session Available for this user"));

        session.setStatus(status);
        sessionRepo.save(session);

    }

    @Transactional
    public void deleteSession(Long userId, Long sessionId) {
        Sessions session = sessionRepo.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
        sessionRepo.delete(session);
    }
}
