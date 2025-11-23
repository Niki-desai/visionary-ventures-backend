package com.jobbot.repository;

import com.jobbot.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByUserId(String userId);
    List<Application> findByJobId(String jobId);
    List<Application> findByUserIdAndJobId(String userId, String jobId);
    
    @Query("{ 'userId': ?0, 'status.current': ?1 }")
    List<Application> findByUserIdAndStatus(String userId, String status);
    
    @Query("{ 'userId': ?0, 'submittedAt': { $gte: ?1 } }")
    List<Application> findByUserIdAndSubmittedAfter(String userId, LocalDateTime date);
    
    @Query("{ 'applicationMethod': ?0 }")
    List<Application> findByApplicationMethod(String method);
    
    boolean existsByUserIdAndJobId(String userId, String jobId);
}

