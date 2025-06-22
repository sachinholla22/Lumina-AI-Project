package com.lumina.ai.Lumina_Ai_Backend.entity;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Email
private String email;
private String password;
private String name;
private String loginType;
private LocalDateTime createdAt = LocalDateTime.now();



}
