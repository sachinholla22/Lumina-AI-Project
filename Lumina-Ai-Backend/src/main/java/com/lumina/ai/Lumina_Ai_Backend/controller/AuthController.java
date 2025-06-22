package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.AuthRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    

    private final AuthService service;
    public AuthController(AuthService service){
        this.service=service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerController(@RequestBody @Valid AuthRequest request ){
        AuthResponse response= service.registerRequest(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.loginRequest(request));
    }


    @GetMapping("/register")
    public String check(){
        return "checking";
    }

 
}
