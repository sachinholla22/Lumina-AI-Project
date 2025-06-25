package com.lumina.ai.Lumina_Ai_Backend.service;

import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.entity.Users;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.UserRepository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
