package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
    
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
private String email;
private String password;
private String name;
private LocalDateTime createdAt = LocalDateTime.now();



}
