package com.lumina.ai.Lumina_Ai_Backend.dto;


import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiError implements Serializable {
    
    private int status;
    private String error;
    private String message;
    private String errorCode;
    
    public ApiError(HttpStatus status, String message, String errorCode) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.errorCode = errorCode;
    }

}
