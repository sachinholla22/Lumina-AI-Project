package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromptResponse {
    
    @JsonIgnore
    private String input;
    private String response;
    private LocalDateTime timestamp;
    private String feedback;
    private boolean isResearchRelated;
}
