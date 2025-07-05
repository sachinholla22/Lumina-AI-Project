package com.lumina.ai.Lumina_Ai_Backend.controller;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.AnonymousService;

import io.github.bucket4j.Bucket;

@RestController
@RequestMapping("/api/anonymous")
public class AnoynomousController {
    
    private final AnonymousService service;
    private final Bucket rateLimit;


    public AnoynomousController(AnonymousService service, Bucket rateLimit){
this.service=service;
this.rateLimit=rateLimit;
    }

    @PostMapping("/prompt")
    public ResponseEntity<ApiResponse<PromptResponse>> processPrompt(@RequestBody PromptRequest request){
        if(!rateLimit.tryConsume(1)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ApiResponse.error(HttpStatus.TOO_MANY_REQUESTS,"Rate Limit Exceeded","RATE_LIMIT_EXCEEDED"));
        }
      
        PromptResponse response=service.processAnoynomousRequest(request.getInput());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
