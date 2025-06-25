package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sessions")
public class Sessions {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @NotNull
    @Size(min=1, max=100, message="Session name must be between 1 and 100 characters")
    private String sessionName;
    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(length=10,nullable=false)
    @NotNull(message="Status cannot be empty")
    private Status status;

    public enum Status{
        ACTIVE,EXPIRED,INACTIVE
    }



}
