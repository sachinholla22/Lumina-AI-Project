package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

@NotNull
@NotBlank(message="Email Should not be empy")
@Email(message = "Email should be valid")
@Column(nullable = false, unique = true)
private String email;
private String password;
private String name;

@Column(unique=true)
private String googleId;

private String loginType;
private LocalDateTime createdAt = LocalDateTime.now();



}
