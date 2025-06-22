package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.PromptService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

@RestController 
@RequestMapping("/api/prompt")
public class PromptController {

    private final PromptService service;
    private final JwtUtil jwtUtil;

    public PromptController(PromptService promptService, JwtUtil jwtUtil) {
        this.service = promptService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping
    public ResponseEntity<PromptResponse> processPrompt(@RequestHeader("Authorization") String authHeader,@RequestBody String request){
        String jwt = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(jwt);
      
        PromptResponse response=service.processAuthenticationPrompt(userId, request);
        return ResponseEntity.ok(response);
    }
    
}
