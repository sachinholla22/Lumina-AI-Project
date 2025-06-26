package com.lumina.ai.Lumina_Ai_Backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Sessions, Long> {
   Optional< Sessions> findByUserIdAndStatus(Long userid,Sessions.Status status);
   List<Sessions> findAllByUserIdAndStatus(Long userId, Sessions.Status status);
   Optional<List<Sessions>> findAllByUserId(Long userId);
}
