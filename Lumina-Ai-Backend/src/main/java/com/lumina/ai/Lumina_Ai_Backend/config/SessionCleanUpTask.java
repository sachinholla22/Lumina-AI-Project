package com.lumina.ai.Lumina_Ai_Backend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import java.util.List;

@Component
public class SessionCleanUpTask {
    
    private final SessionRepository sessionRepo;

    public SessionCleanUpTask(SessionRepository sessionRepo){
        this.sessionRepo=sessionRepo;
    }

    @Scheduled(fixedRate=3600000)
    public void cleanUpSessions(){
        List<Sessions> sessions=sessionRepo.findAll();

        LocalDateTime now=LocalDateTime.now();


        for(Sessions session:sessions){
            if(session.getStatus()==Sessions.Status.ACTIVE){
             if(session.getLastUpdated().isBefore(now.minusMinutes(30))){
                session.setStatus(Sessions.Status.INACTIVE);
                sessionRepo.save(session);
             }
            }

            if(session.getCreatedAt().isBefore(now.minusHours(24))){
                session.setStatus(Sessions.Status.EXPIRED);
                sessionRepo.save(session);

            }
        }
    }

}
