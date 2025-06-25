package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.ChatService;
import com.lumina.ai.Lumina_Ai_Backend.service.PromptService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import io.jsonwebtoken.lang.Collections;

@RestController 
@RequestMapping("/api/prompt")
public class PromptController {

    private final PromptService service;
    private final JwtUtil jwtUtil;
    private final ChatService chatService;

    public PromptController(PromptService promptService, JwtUtil jwtUtil,ChatService chatService) {
        this.service = promptService;
        this.jwtUtil = jwtUtil;
        this.chatService=chatService;
    }


    @PostMapping
    public ResponseEntity<PromptResponse> processPrompt(@RequestHeader("Authorization") String authHeader,@RequestBody String request){
        String jwt = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(jwt);
      
        PromptResponse response=service.processAuthenticationPrompt(userId, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getChatHistory")
    public ResponseEntity<List<ChatResponse>> getChatHistory(@RequestHeader("Authorization") String authHeader){
        String jwt=authHeader.substring(7);
        String userId=jwtUtil.extractUserId(jwt);
        Optional <List<ChatResponse>>history=chatService.getChatHistory(userId);
        return ResponseEntity.ok(history.orElse(Collections.emptyList()));
    }
    
}
