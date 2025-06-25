package com.lumina.ai.Lumina_Ai_Backend.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

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

    public String aiUrl="http://localhost:5000/q";

    public PromptService(ChatRepository chatRepo,SessionRepository sessionRepo,RestTemplate template){
this.chatRepo=chatRepo;
this.sessionRepo=sessionRepo;
this.template=template;
    }

    public PromptResponse processAuthenticationPrompt( String userId,String input){
    if(input==null||input.trim().isEmpty()){
        throw new IllegalArgumentException("Prompt cannot be Empty");
    }

    Sessions session=sessionRepo.findByUserIdAndStatus(Long.valueOf(userId),Sessions.Status.ACTIVE).orElse(null);
                   // .orElseThrow(()->new IllegalArgumentException("No Active Session Found"));

    HttpHeaders header=new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);   
    
    PromptRequest request=new PromptRequest();
    request.setInput(input);

    HttpEntity<PromptRequest>entity=new HttpEntity<>(request,header);

    PromptResponse response=template.postForObject(aiUrl,entity,PromptResponse.class);


    Chats chat=new Chats();
    chat.setSession(session);
    chat.setInput(input);
    chat.setResponse(response.getResponse());
    chatRepo.save(chat);

    return response;

    }
}
