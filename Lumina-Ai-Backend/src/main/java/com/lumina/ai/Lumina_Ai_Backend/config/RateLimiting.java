package com.lumina.ai.Lumina_Ai_Backend.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Configuration
public class RateLimiting {
    

    @Bean 
    public Bucket rateLimiBucket(){
        Bandwidth limit=Bandwidth.classic(10,Refill.greedy(10,Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}
