package com.lumina.ai.Lumina_Ai_Backend.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean("taskExecutor")
    public Executor asyncTaskExecutor(){
        ThreadPoolTaskExecutor tasks=new ThreadPoolTaskExecutor();
        tasks.setCorePoolSize(4);
        tasks.setQueueCapacity(150);
        tasks.setMaxPoolSize(4);
        tasks.setThreadNamePrefix("AsyncTaskThread-");
        tasks.initialize();
        return tasks;

    }
}
