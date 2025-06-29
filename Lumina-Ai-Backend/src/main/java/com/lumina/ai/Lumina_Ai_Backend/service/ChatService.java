package com.lumina.ai.Lumina_Ai_Backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lumina.ai.Lumina_Ai_Backend.Exception.ResourceNotFoundException;
import com.lumina.ai.Lumina_Ai_Backend.dto.ChatResponse;
import com.lumina.ai.Lumina_Ai_Backend.entity.Chats;
import com.lumina.ai.Lumina_Ai_Backend.repo.ChatRepository;
import com.lumina.ai.Lumina_Ai_Backend.repo.SessionRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {
    
    private final ChatRepository chatRepository;
    private final SessionRepository sessionRepo;
    public ChatService(ChatRepository chatRepository,SessionRepository sessionRepo) {
        this.chatRepository = chatRepository;
        this.sessionRepo = sessionRepo;
    }
@Transactional(readOnly = true)
    public Optional<List<ChatResponse>> getChatHistory(String userId){
        Optional<List<Chats>>chats = chatRepository.findChatsByUserId(Long.valueOf(userId));
      return chats.map(chatList -> chatList.stream()
                .map(chat -> new ChatResponse(
                       
                        chat.getInput(),
                        chat.getResponse()))
                .collect(Collectors.toList()));
    }


    @Transactional(readOnly=true)
    public List<ChatResponse> getChatsBySessions(Long userId,Long sessionId){
    sessionRepo.findByIdAndUserId(sessionId,userId).orElseThrow(()->new IllegalArgumentException("No session available"));

    List<Chats> chats=chatRepository.findBySessionId(sessionId);
    return chats.stream().map(chat->new ChatResponse(
      
        chat.getInput(),
        chat.getResponse()))
        .collect(Collectors.toList());
    
    }

    @Transactional
    public void deleteChat(Long userId, Long chatId) {
        chatRepository.deleteByIdAndSessionUserId(chatId, userId);
    }


@Transactional
    public void updateChat(Long userId, Long chatId, String userPrompt, Boolean isResearchRelated) {
        Chats chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        if (!chat.getSession().getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Unauthorized to update chat");
        }
        if (userPrompt != null && !userPrompt.isBlank()) {
            chat.setInput(userPrompt);
        }
       
        chatRepository.save(chat);
    }
}
