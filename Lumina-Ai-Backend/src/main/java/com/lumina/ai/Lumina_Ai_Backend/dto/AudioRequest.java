package com.lumina.ai.Lumina_Ai_Backend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioRequest {
    
    private MultipartFile audioFile;
    private String instruction;
}
