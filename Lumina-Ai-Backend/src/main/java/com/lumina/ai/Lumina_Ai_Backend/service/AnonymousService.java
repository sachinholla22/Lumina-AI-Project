package com.lumina.ai.Lumina_Ai_Backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;

@Service
public class AnonymousService {
    
    private final RestTemplate restTemplate;
    private final String aiServieUrl="http://localhost:5000/api/prompt";


    public AnonymousService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }
    public PromptResponse processAnoynomousRequest(String prompt){
        if(prompt==null || prompt.trim().isEmpty()){
            throw new IllegalArgumentException("Prompt Cannot be empty");
        }
        PromptRequest request=new PromptRequest();
        request.setPrompt(prompt);
        PromptResponse response=restTemplate.postForObject(aiServieUrl,request,PromptResponse.class);
        return response;
    }



}
