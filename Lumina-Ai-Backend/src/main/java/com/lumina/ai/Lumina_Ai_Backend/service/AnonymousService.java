package com.lumina.ai.Lumina_Ai_Backend.service;

import java.net.http.HttpHeaders;
import java.util.concurrent.CompletableFuture;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;

@Service
public class AnonymousService {
    
    private final RestTemplate restTemplate;
    private final String aiServiceUrl="http://localhost:5000/q";


    public AnonymousService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }
    @Async("taskExecutor")
    public CompletableFuture<PromptResponse> processAnoynomousRequest(String prompt){
        if(prompt==null || prompt.trim().isEmpty()){
            throw new IllegalArgumentException("Prompt Cannot be empty");
        }
        PromptRequest request=new PromptRequest();
        request.setInput(prompt);
        PromptResponse response=restTemplate.postForObject(aiServiceUrl,request,PromptResponse.class);
        return CompletableFuture.completedFuture(response);
    }



}
