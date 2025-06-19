package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllAr
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;gsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Sessions {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;
    private String sessionName;
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();
    private String status;

}
