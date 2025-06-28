package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.SessionResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.service.ChatService;
import com.lumina.ai.Lumina_Ai_Backend.service.SessionService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import io.jsonwebtoken.lang.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    
    private final SessionService service;
    private final JwtUtil jwtUtil;
    private final ChatService chatService;
    public SessionController(SessionService service,JwtUtil jwtUtil,ChatService chatService){
        this.service=service;
        this.jwtUtil=jwtUtil;
        this.chatService=chatService;
    }

    @GetMapping("/new")
    public ResponseEntity<String> creatNewSession(@RequestHeader("Authorization") String authHeader){
       String jwt=authHeader.replace("Bearer ","");
       String userId=jwtUtil.extractUserId(jwt);
       Sessions session=service.createNewSession(userId);
       return ResponseEntity.status(HttpStatus.OK).body("created new Session");
    }


    @GetMapping
    public ResponseEntity<List<SessionResponse>> getSessionHistory(@RequestHeader("Authorization") String authHeader){
         String jwt=authHeader.replace("Bearer ","");
       String userId=jwtUtil.extractUserId(jwt);
      Optional<List<SessionResponse>> items=service.getSessionHistoryDetails(userId);
       return ResponseEntity.ok(items.orElse(Collections.emptyList()));
    }


@GetMapping("{sessionId}/chats")
    public ResponseEntity<List<ChatResponse>> getChatsBySession(
            @PathVariable Long sessionId,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));
        return ResponseEntity.ok(chatService.getChatsBySessions(userId, sessionId));
    }

}
