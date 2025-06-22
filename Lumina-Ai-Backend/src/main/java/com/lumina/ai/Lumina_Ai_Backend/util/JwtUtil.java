package com.lumina.ai.Lumina_Ai_Backend.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKeySpec secretKeySpec(){
        return new SecretKeySpec(secret.getBytes(),"HmacSHA256");
    }

    public String generateToken(String userId){
        return Jwts.builder()
                   .setSubject(userId)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis()+expiration))
                   .signWith(secretKeySpec())
                   .compact();
    }

    public boolean isTokenValid(String token){
        try {
            Jwts.parser().setSigningKey(secretKeySpec()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
        public String extractUserId(String token){
            return Jwts.parser().setSigningKey(secretKeySpec()).build()
                       .parseClaimsJws(token).getBody().getSubject();
        }
    
    }

