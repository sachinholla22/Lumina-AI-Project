package com.lumina.ai.Lumina_Ai_Backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;

@Repository
public interface ChatRepository  extends JpaRepository<Chats,Long> {
    
}
