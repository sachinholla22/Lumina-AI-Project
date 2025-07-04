package com.lumina.ai.Lumina_Ai_Backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Pattern;

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
import com.lumina.ai.Lumina_Ai_Backend.dto.AudioRequest;
import com.lumina.ai.Lumina_Ai_Backend.dto.PromptResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;
import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import com.lumina.ai.Lumina_Ai_Backend.repo.ChatRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

@Service
public class AudioService {
    private final ChatRepository chatRepo;
    private final SessionRepository sessionRepo;
    private final RestTemplate template;
    private final JwtUtil jwtUtil;
    private final String audioUrl = "http://localhost:5000/audio";
    private static final long SESSION_EXPIRY_HOURS = 24;
    private static final Pattern INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s.,!?']{1,500}$");

    public AudioService(ChatRepository chatRepo, SessionRepository sessionRepo, RestTemplate template, JwtUtil jwtUtil) {
        this.chatRepo = chatRepo;
        this.sessionRepo = sessionRepo;
        this.template = template;
        this.jwtUtil = jwtUtil;
    }
    public ApiResponse<PromptResponse> processAudioPrompt(String jwt,AudioRequest request){
        if(audioFile==null|| audioFile.isEmpty()) {
            throw new IllegalArgumentException("Audio file cannot be empty");
        }
        if (!jwtUtil.isTokenValid(jwt)) {
            throw new IllegalArgumentException("Invalid or expired JWT");
        }
        Long userId = Long.valueOf(jwtUtil.extractUserId(jwt));
        Optional<Sessions> activeSession = sessionRepo.findByUserIdAndStatus(userId, Sessions.Status.ACTIVE);
        Sessions session = activeSession.orElseThrow(() -> new IllegalArgumentException("No active session found"));
        if (session.getCreatedAt().isBefore(LocalDateTime.now().minus(SESSION_EXPIRY_HOURS, ChronoUnit.HOURS))) {
            session.setStatus(Sessions.Status.INACTIVE);
            sessionRepo.save(session);
            throw new IllegalArgumentException("Session has expired");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> map=new LinkedMultiValueMap<>();
        try {
           map.add("audioFile",new HttpEntity<>(request.getAudioFile().getBytes()));
           body.add("instruction", request.getInstruction());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to read audio file: " + e.getMessage());
        }
        HttpEntity<MultiValueMap<String,Object>>entity=new HttpEntity<>(map,headers);
       PromptResponse microserviceResponse;
        try {
            microserviceResponse = template.postForObject(audioUrl, entity, PromptResponse.class);
            if (microserviceResponse == null) {
                return ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE, "Audio service returned no response", "MICROSERVICE_ERROR");
            }
        } catch (RestClientException e) {
            return ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE, "Audio service unavailable: " + e.getMessage(), "MICROSERVICE_UNAVAILABLE");
        }

        if (!INPUT_PATTERN.matcher(microserviceResponse.getInput()).matches()) {
            throw new IllegalArgumentException("Transcribed text contains invalid characters or exceeds 500 characters");
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

