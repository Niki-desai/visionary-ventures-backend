package com.jobbot.repository;

import com.jobbot.model.AIConversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIConversationRepository extends MongoRepository<AIConversation, String> {
    List<AIConversation> findByUserId(String userId);
    List<AIConversation> findByUserIdAndIsActiveTrue(String userId);
    
    @Query("{ 'userId': ?0, 'conversationType': ?1 }")
    List<AIConversation> findByUserIdAndConversationType(String userId, String conversationType);
    
    @Query("{ 'userId': ?0, 'context.jobId': ?1 }")
    List<AIConversation> findByUserIdAndJobId(String userId, String jobId);
}

