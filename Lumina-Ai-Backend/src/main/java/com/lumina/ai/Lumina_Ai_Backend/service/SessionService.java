package com.lumina.ai.Lumina_Ai_Backend.service;

import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import org.springframework.stereotype.Service;
@Service
public class SessionService {
    
    private  final UserRepository userRepo;
    private  final SessionRepository sessionRepo;

    public Sessions creatwNewSession(String userId){
        Users user=userRepo.findById(Long.valueOf(userId))
                  .orElseThrow(()->new IllegalArgumentException("User Not Found"));
               
        Sessions session=new Sessions();
        session.setUser(user);
        session.setSessionName("New Chat "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
        session.setStatus(Sessions.Status.ACTIVE);
        return sessionRepo.save(session);          
    }
}
