package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
   
    private String input;
   
    private String response;
}
