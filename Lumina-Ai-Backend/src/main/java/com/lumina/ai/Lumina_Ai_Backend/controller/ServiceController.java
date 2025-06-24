package com.lumina.ai.Lumina_Ai_Backend.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;
@RestController
@RequestMapping("/api/sessions")
public class ServiceController {
    
    private final SessionService service;
    private final JwtUtil jwtUtil;

    public ServiceController(SessionService service,JwtUtil jwtUtil){
        this.service=service;
        this.jwtUtil=jwtUtil;
    }

    @GetMapping("/new")
    public ResponseEntity<Sessions> creatNewSession(@RequestHeader("Authorization") String authHeader){
       String jwt=authHeader.replace("Bearer ","");
       String userId=jwtUtil.extractUserId(jwt);
       Sessions session=service.createNewSession(userId);
       return ResponseEntity.ok(session);
    }
}
