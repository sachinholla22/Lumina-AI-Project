package com.lumina.ai.Lumina_Ai_Backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.lumina.ai.Lumina_Ai_Backend.dto.AuthRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.repo.ChatRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;

@Service
public class PromptService {
    
    private final ChatRepository chatRepo;
    private final SessionRepository sessionRepo;
    private final RestTemplate template;
    private static final int SESSION_EXPIRY_HOURS=24;


    public String aiUrl="http://localhost:5000/q";

    public PromptService(ChatRepository chatRepo,SessionRepository sessionRepo,RestTemplate template){
this.chatRepo=chatRepo;
this.sessionRepo=sessionRepo;
this.template=template;
    }

    @Async("taskExecutor")
    public CompletableFuture<PromptResponse> processAuthenticationPrompt( String userId,String input){
    if(input==null||input.trim().isEmpty()){
        throw new IllegalArgumentException("Prompt cannot be Empty");
    }

    Sessions session=sessionRepo.findByUserIdAndStatus(Long.valueOf(userId),Sessions.Status.ACTIVE)
                   .orElseThrow(()->new IllegalArgumentException("No Active Session Found"));

    if(session.getCreatedAt().isBefore(LocalDateTime.now().minus(SESSION_EXPIRY_HOURS, ChronoUnit.HOURS))){
        session.setStatus(Sessions.Status.INACTIVE);
        sessionRepo.save(session);
        throw new IllegalArgumentException("Session has expired");
    }            

    HttpHeaders header=new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);   
    
    PromptRequest request=new PromptRequest();
    request.setId(Long.valueOf(userId));
    request.setInput(input);

    HttpEntity<PromptRequest>entity=new HttpEntity<>(request,header);

    PromptResponse response=template.postForObject(aiUrl,entity,PromptResponse.class);
 response.setTimestamp(LocalDateTime.now());

    Chats chat=new Chats();
    chat.setSession(session);
    chat.setInput(input);
    chat.setResponse(response.getResponse());
    chatRepo.save(chat);

    if(session.getSessionName().startsWith("Google_Login_") || session.getSessionName().startsWith("Session_")|| session.getSessionName().startsWith("New Chat ")){
     session.setSessionName(response.getSessionTitle() != null ? response.getSessionTitle() : "Untitled_Session");
    sessionRepo.save(session);
    }

 return CompletableFuture.completedFuture(new PromptResponse(   
    request.getInput(),
    response.getResponse(),
    response.getTimestamp(),
    response.getFeedback(),
     chat.getId(),
      session.getId(),
      response.getSessionTitle(),
    response.isResearchRelated()
 ));

    }
}
