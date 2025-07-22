package com.lumina.ai.Lumina_Ai_Backend.controller;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

     @PostMapping(value = "/prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter processPrompt(@RequestBody PromptRequest request){
         SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        if(!rateLimit.tryConsume(1)){
             emitter.completeWithError(new RuntimeException("Rate Limit Exceeded"));
            return emitter;
        }
       try {
            emitter.send(SseEmitter.event().name("status").data("Processing..."));
            service.processAnoynomousRequest(request.getInput())
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

}
