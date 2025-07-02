package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.AuthResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.SetPasswordRequest;
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
    public ResponseEntity<ApiResponse<AuthResponse>> registerController(@RequestBody @Valid AuthRequest request ){
        AuthResponse response= service.registerRequest(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.loginRequest(request)));
    }

    @GetMapping("/google/success")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLoginSuccess(OAuth2AuthenticationToken authentication){
if (!"google".equals(authentication.getAuthorizedClientRegistrationId())) {
            return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"BInvalid OAuth2 provider, expected Google","INVALID_CLIENT"));
        }
        return ResponseEntity.ok(ApiResponse.success(service.handleGoogleLogin(authentication)));
    }

    @GetMapping("/google/failure")
    public ResponseEntity<ApiResponse<String>> googleLoginFailure() {
        return ResponseEntity.status(401).body(ApiResponse.error(HttpStatus.UNAUTHORIZED,"User Not Authorized","INVALID_USER"));
    }


    @PostMapping("/setpassword")
    public ResponseEntity<ApiResponse<String>> setPassword(@RequestBody SetPasswordRequest request, @RequestHeader("Authorization")String authHead){
       
            String jwt = authHead.replace("Bearer ", "");
            service.setPassword(jwt, request.getPassword());
            return ResponseEntity.status(201).body(ApiResponse.success("Password Set Successfully"));
       
    }
 
}
