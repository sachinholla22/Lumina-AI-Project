package com.lumina.ai.Lumina_Ai_Backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Pattern;


import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;
import com.lumina.ai.Lumina_Ai_Backend.dto.OcrRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.repo.ChatRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.UserRepository;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

import jakarta.transaction.Transactional;


@Service
public class OcrService {

    private final ChatRepository chatRepo;
    private final JwtUtil jwtUtil;
    private  final String OcrUrl="http://localhost:5000/ocr";
    private final SessionRepository sessionRepo;
    private final RestTemplate template;
    private static final long SESSION_EXPIRY_HOURS=24;
    private static final Pattern INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,!?']{1,500}$");

    public OcrService(ChatRepository chatRepo, JwtUtil jwtUtil, RestTemplate template, SessionRepository sessionRepo){
        this.chatRepo=chatRepo;
        this.jwtUtil=jwtUtil;
        this.template=template;
        this.sessionRepo=sessionRepo;
    }

    @Transactional
    public ApiResponse<PromptResponse> processOcrRequest(String jwt, OcrRequest request){
        if (request == null ) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }
        if(!jwtUtil.isTokenValid(jwt)){
            throw new IllegalArgumentException("Invalid input or expired JWT");
        }

        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));

        Optional<Sessions> activeSessions=sessionRepo.findByUserIdAndStatus(userId, Sessions.Status.ACTIVE);
        Sessions session=activeSessions.orElseThrow(()-> new IllegalArgumentException("Invalid session"));

        if(session.getCreatedAt().isBefore(LocalDateTime.now().minus(SESSION_EXPIRY_HOURS, ChronoUnit.HOURS))){
            session.setStatus(Sessions.Status.INACTIVE);
            sessionRepo.save(session);
            throw new IllegalArgumentException("Session has expired");
        }

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try{
          body.add("imageFile", new HttpEntity<>(request.getImageFile().getBytes()));
            body.add("instruction", request.getInstruction());

        }catch (Exception e) {
            throw new IllegalArgumentException("Failed to read image file: " + e.getMessage());
        }
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
       PromptResponse microserviceResponse;
        try {
            microserviceResponse = template.postForObject(OcrUrl, entity, PromptResponse.class);
            if (microserviceResponse == null) {
                return ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE, "OCR service returned no response", "MICROSERVICE_ERROR");
            }
        } catch (RestClientException e) {
            return ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE, "OCR service unavailable: " + e.getMessage(), "MICROSERVICE_UNAVAILABLE");
        }
        if(!INPUT_PATTERN.matcher(microserviceResponse.getInput()).matches()){
            throw new IllegalArgumentException("Extracted text contains invalid characters or exceeds 500 characters");
        }
        Chats chat = new Chats();
        chat.setSession(session);
        chat.setInput(microserviceResponse.getInput());
        chat.setResponse(microserviceResponse.getResponse());
        chat.setInstruction(request.getInstruction());
        chatRepo.save(chat);

        return ApiResponse.success(microserviceResponse);
    }

    
    
    
}
