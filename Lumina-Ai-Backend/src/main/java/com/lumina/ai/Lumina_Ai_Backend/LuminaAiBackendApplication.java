package com.lumina.ai.Lumina_Ai_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LuminaAiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminaAiBackendApplication.class, args);
    }
}