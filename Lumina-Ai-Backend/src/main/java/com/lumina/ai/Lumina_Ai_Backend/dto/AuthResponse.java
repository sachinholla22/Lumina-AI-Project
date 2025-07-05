package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse implements Serializable{
    private String jwt;
    private String userId;
}
