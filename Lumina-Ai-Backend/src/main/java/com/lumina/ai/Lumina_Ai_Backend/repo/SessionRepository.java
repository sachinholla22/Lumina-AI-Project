package com.lumina.ai.Lumina_Ai_Backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lumina.ai.Lumina_Ai_Backend.entity.Sessions;
import java.util.List;

public interface SessionRepository extends JpaRepository<Sessions, Long> {
    Sessions findByUserIdAndStatus(Long userid,Sessions.Status status);
}
