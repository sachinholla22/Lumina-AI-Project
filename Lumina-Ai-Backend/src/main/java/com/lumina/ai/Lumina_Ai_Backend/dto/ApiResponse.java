package com.lumina.ai.Lumina_Ai_Backend.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse<T> implements Serializable {
    
    private boolean success;
    private T data;
    private ApiError error;


    public static<T>  ApiResponse<T> success(T data){
       ApiResponse<T> response=new ApiResponse<>();
       response.setSuccess(true);
       response.setData(data);
       return response;
    }

    public static <T> ApiResponse<T> error(HttpStatus status,String message, String errorCode){
        ApiResponse<T> response=new ApiResponse<>();
        response.setSuccess(false);
        response.setError(new ApiError(status,message,errorCode));
        return response;
    }

}
