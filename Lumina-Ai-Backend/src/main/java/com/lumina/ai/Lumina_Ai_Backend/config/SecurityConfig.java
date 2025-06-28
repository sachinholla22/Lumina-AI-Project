package com.lumina.ai.Lumina_Ai_Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lumina.ai.Lumina_Ai_Backend.util.JwtAuthFilter;
import com.lumina.ai.Lumina_Ai_Backend.util.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
          
            .csrf().disable()
            .authorizeHttpRequests(req->req.requestMatchers("/api/anonymous/**","/api/auth/register","/api/auth/login","/login/oauth2/code/google").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated())
            .oauth2Login(auth->auth.defaultSuccessUrl("/api/auth/google/success",true).failureUrl("/api/auth/google/failure"))
            .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }



}
