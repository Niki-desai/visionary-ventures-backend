package com.jobbot.repository;

import com.jobbot.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    
    // Find all chats for a user
    List<Chat> findByUserId(String userId);
    
    // Find active chats for a user
    List<Chat> findByUserIdAndIsActiveTrue(String userId);
    
    // Find pinned chats for a user
    List<Chat> findByUserIdAndIsPinnedTrue(String userId);
    
    // Find chats by type
    List<Chat> findByUserIdAndChatType(String userId, String chatType);
    
    // Find chat by user and chat ID (for security)
    Optional<Chat> findByIdAndUserId(String id, String userId);
    
    // Search chats by title
    @Query("{ 'userId': ?0, 'title': { $regex: ?1, $options: 'i' } }")
    List<Chat> findByUserIdAndTitleContaining(String userId, String title);
    
    // Get chats ordered by last message (most recent first)
    Page<Chat> findByUserIdOrderByLastMessageAtDesc(String userId, Pageable pageable);
    
    // Get pinned chats first, then by last message
    @Query("{ 'userId': ?0 }")
    List<Chat> findByUserIdOrderByIsPinnedDescLastMessageAtDesc(String userId);
    
    // Count active chats for a user
    long countByUserIdAndIsActiveTrue(String userId);
}

