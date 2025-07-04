package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.AudioRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.AudioService;

import io.github.bucket4j.Bucket;

@RestController
@RequestMapping("/api/audio")
public class AudioController {
    

    private final AudioService audioService;
    private final Bucket rateLimitBucket;
    public AudioController(AudioService audioService,Bucket rateLimitBucket){
        this.audioService=audioService;
        this.rateLimitBucket=rateLimitBucket;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromptResponse>> processAudioPrompt(@RequestHeader("Authorization") String header,@RequestPart("audioFile") MultipartFile audioFile,@RequestPart("instruction") String instruction){
        if (!rateLimitBucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded", "RATE_LIMIT_EXCEEDED"));
        }
        String jwt = header.replace("Bearer ", "");
        AudioRequest request = new AudioRequest();
        request.setAudioFile(audioFile);
        request.setInstruction(instruction);
        return ResponseEntity.ok(audioService.processAudioPrompt(jwt, request));
    }
}
