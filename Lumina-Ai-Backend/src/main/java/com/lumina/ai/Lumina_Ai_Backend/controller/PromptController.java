package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.ChatService;
import com.lumina.ai.Lumina_Ai_Backend.service.PromptService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import io.github.bucket4j.Bucket;
import io.jsonwebtoken.lang.Collections;


@RestController 
@RequestMapping("/api/prompt")
public class PromptController {

    private final PromptService service;
    private final JwtUtil jwtUtil;
    private final ChatService chatService;
    private final Bucket rateLimit;

    public PromptController(PromptService promptService, JwtUtil jwtUtil,ChatService chatService, Bucket rateLimit) {
        this.service = promptService;
        this.jwtUtil = jwtUtil;
        this.chatService=chatService;
        this.rateLimit=rateLimit;
    }


    @PostMapping(produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter processPrompt(@RequestHeader("Authorization") String authHeader,@RequestBody String request){
         SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        String jwt = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(jwt);
        if(!jwtUtil.isTokenValid(jwt)){
            throw new IllegalArgumentException("Invalid or expired JWT");
        }
      
       if(!rateLimit.tryConsume(1)){
           emitter.completeWithError(new RuntimeException("Rate Limit Exceeded"));
            return emitter;
        }
       try {
            emitter.send(SseEmitter.event().name("status").data("Processing..."));
            service.processAuthenticationPrompt(userId, request.getInput())
                .thenAccept(response -> {
                    try {
                        emitter.send(SseEmitter.event().name("response").data(ApiResponse.success(response)));
                        emitter.complete();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .exceptionally(throwable -> {
                    emitter.completeWithError(throwable);
                    return null;
                });
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }


    @GetMapping("/getChatHistory")
    public ResponseEntity<ApiResponse<List<ChatResponse>>> getChatHistory(@RequestHeader("Authorization") String authHeader){
        String jwt=authHeader.substring(7);
        String userId=jwtUtil.extractUserId(jwt);
        if(!jwtUtil.isTokenValid(jwt)){
            throw new IllegalArgumentException("Invalid or expired JWT");
        }
        Optional <List<ChatResponse>>history=chatService.getChatHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(history.orElse(Collections.emptyList())));
    }
    
}
