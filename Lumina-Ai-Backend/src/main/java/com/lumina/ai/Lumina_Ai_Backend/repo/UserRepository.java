package com.lumina.ai.Lumina_Ai_Backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumina.ai.Lumina_Ai_Backend.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
    Optional<Users> findByEmail(String email);
}
