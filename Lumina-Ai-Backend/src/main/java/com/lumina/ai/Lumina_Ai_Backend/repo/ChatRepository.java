package com.lumina.ai.Lumina_Ai_Backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ChatRepository  extends JpaRepository<Chats,Long> {
    
    @Query(value="SELECT c.* FROM chats c " +
                   "JOIN sessions s ON c.session_id = s.id " +
                   "WHERE s.user_id = :userId " +
                   "ORDER BY c.created_at DESC",nativeQuery=true)
                   List<Chats> findChatsByUserId(@Param("userId")Long userId);
}
