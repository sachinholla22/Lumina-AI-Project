package com.lumina.ai.Lumina_Ai_Backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;
import com.lumina.ai.Lumina_Ai_Backend.repo.ChatRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
@Transactional(readOnly = true)
    public Optional<List<ChatResponse>> getChatHistory(String userId){
        Optional<List<Chats>>chats = chatRepository.findChatsByUserId(Long.valueOf(userId));
      return chats.map(chatList -> chatList.stream()
                .map(chat -> new ChatResponse(
                        chat.getId(),
                        chat.getInput(),
                        chat.getResponse(),
                        chat.getCreatedAt(),
                        chat.getSession().getId()))
                .collect(Collectors.toList()));
    }
}
