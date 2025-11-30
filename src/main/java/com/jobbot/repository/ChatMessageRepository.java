package com.jobbot.repository;

import com.jobbot.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    
    // Find all messages in a chat
    List<ChatMessage> findByChatIdOrderByCreatedAtAsc(String chatId);
    
    // Find messages with pagination
    Page<ChatMessage> findByChatIdOrderByCreatedAtDesc(String chatId, Pageable pageable);
    
    // Find recent messages (for loading chat history)
    List<ChatMessage> findByChatIdOrderByCreatedAtDesc(String chatId, Pageable pageable);
    
    // Find messages by role
    List<ChatMessage> findByChatIdAndRole(String chatId, String role);
    
    // Find messages after a certain time (for real-time updates)
    List<ChatMessage> findByChatIdAndCreatedAtAfter(String chatId, LocalDateTime after);
    
    // Count messages in a chat
    long countByChatId(String chatId);
    
    // Find messages by user (across all chats)
    List<ChatMessage> findByUserId(String userId);
    
    // Find last message in a chat
    ChatMessage findFirstByChatIdOrderByCreatedAtDesc(String chatId);
    
    // Delete all messages in a chat
    void deleteByChatId(String chatId);
    
    // Find messages by parent (for threaded conversations)
    List<ChatMessage> findByParentMessageId(String parentMessageId);
}

