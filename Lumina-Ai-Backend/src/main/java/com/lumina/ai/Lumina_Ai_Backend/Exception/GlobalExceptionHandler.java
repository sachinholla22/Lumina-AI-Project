package com.lumina.ai.Lumina_Ai_Backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lumina.ai.Lumina_Ai_Backend.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex){
    return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,ex.getMessage(),"INVALID INPUT"));

 }

 @ExceptionHandler(ResourceNotFoundException.class)
 public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(HttpStatus.NOT_FOUND,e.getMessage(),"RESOURCE NOT FOUND"));
 }

 @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "SERVER_ERROR"));
    }
}
