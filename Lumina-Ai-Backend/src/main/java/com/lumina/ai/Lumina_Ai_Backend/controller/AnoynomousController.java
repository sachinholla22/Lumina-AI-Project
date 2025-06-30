package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lumina.ai.Lumina_Ai_Backend.dto.PromptRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.AnonymousService;

@RestController
@RequestMapping("/api/anonymous")
public class AnoynomousController {
    
    private final AnonymousService service;

    public AnoynomousController(AnonymousService service){
this.service=service;
    }

    @PostMapping("/prompt")
    public ResponseEntity<PromptResponse> processPrompt(@RequestBody PromptRequest request){
        PromptResponse response=service.processAnoynomousRequest(request.getInput());
        return ResponseEntity.ok(response);
    }

}
