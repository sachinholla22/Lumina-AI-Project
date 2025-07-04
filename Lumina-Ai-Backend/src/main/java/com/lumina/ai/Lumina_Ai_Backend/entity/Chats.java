package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="chats")
public class Chats {
    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition="TEXT",nullable=false)
    private String input;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="session_id",nullable = false)
    private Sessions session;

    
    @Column(columnDefinition = "TEXT",nullable=false)
    private String response;

    private boolean isResearchRelated;
    
    private String instruction;
    
    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt=LocalDateTime.now();
}
