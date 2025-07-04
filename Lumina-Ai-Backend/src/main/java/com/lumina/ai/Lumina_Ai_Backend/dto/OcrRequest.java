package com.lumina.ai.Lumina_Ai_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OcrRequest {
    
    private MultipartFile imageFile;
    private String instruction;
}
