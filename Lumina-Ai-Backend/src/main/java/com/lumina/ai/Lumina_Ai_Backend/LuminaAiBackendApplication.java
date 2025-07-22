package com.lumina.ai.Lumina_Ai_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync
@SpringBootApplication
public class LuminaAiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuminaAiBackendApplication.class, args);
    }
}