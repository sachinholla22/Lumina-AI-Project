package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private static final Pattern INPUT_PATTERN=Pattern.compile("^[a-zA-Z0-9\\\\s.,!?']{1,500}$");

    public PromptController(PromptService promptService, JwtUtil jwtUtil,ChatService chatService, Bucket rateLimit) {
        this.service = promptService;
        this.jwtUtil = jwtUtil;
        this.chatService=chatService;
        this.rateLimit=rateLimit;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<PromptResponse>> processPrompt(@RequestHeader("Authorization") String authHeader,@RequestBody String request){
        String jwt = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(jwt);
        if(!jwtUtil.isTokenValid(jwt)){
            throw new IllegalArgumentException("Invalid or expired JWT");
        }
        if (!INPUT_PATTERN.matcher(request).matches()) {
            throw new IllegalArgumentException("Input contains invalid characters or exceeds 500 characters");
        }
       if(!rateLimit.tryConsume(1)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ApiResponse.error(HttpStatus.TOO_MANY_REQUESTS,"Rate Limit Exceeded","RATE_LIMIT_EXCEEDED"));
        }
        PromptResponse response=service.processAuthenticationPrompt(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
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
