package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromptResponse {
    

    private String input;
    private String response;
    private LocalDateTime timestamp;
    private String feedback;
    private boolean isResearchRelated;
}
