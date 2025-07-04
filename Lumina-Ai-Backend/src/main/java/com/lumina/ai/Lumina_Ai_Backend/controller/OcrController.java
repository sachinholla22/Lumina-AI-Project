package com.lumina.ai.Lumina_Ai_Backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.OcrRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.service.OcrService;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import io.github.bucket4j.Bucket;

@RestController
@RequestMapping("/api/image")
public class OcrController {
    

    private final OcrService ocrService;
    private final JwtUtil jwtUtil;
    private final Bucket rateLimitBucket;

    public OcrController(OcrService ocrService, JwtUtil jwtUtil, Bucket rateLimitBucket){
    this.ocrService=ocrService;
    this.jwtUtil=jwtUtil;
    this.rateLimitBucket=rateLimitBucket;
    }


    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PromptResponse>> processOcrReq(@RequestHeader("Authorization")String authHeader,MultipartFile imageFile,@RequestPart("instruction") String instruction){
        if(!rateLimitBucket.tryConsume(1)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded", "RATE_LIMIT_EXCEEDED"));
        }
        String jwt = authHeader.replace("Bearer ", "");
        OcrRequest request = new OcrRequest();
        request.setImageFile(imageFile);
        request.setInstruction(instruction);
        return ResponseEntity.ok(ocrService.processOcrRequest(jwt, request));
        
    }

}
