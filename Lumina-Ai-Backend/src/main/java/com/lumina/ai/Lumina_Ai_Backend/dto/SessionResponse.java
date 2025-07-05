package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse implements Serializable{
    
    private Long id;
    private String sessionName;
    private LocalDateTime createdAt;
    private String status;
}
