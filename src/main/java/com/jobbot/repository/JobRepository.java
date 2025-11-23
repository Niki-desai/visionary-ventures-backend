package com.jobbot.repository;

import com.jobbot.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByIsActiveTrue();
    List<Job> findByCompanyId(String companyId);
    List<Job> findByIndustry(String industry);
    List<Job> findByJobType(String jobType);
    List<Job> findByRemoteType(String remoteType);
    List<Job> findByPostedDateAfter(LocalDateTime date);
    
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Job> findByTitleContaining(String title);
    
    @Query("{ 'requiredSkills': { $in: ?0 } }")
    List<Job> findByRequiredSkillsIn(List<String> skills);
    
    @Query("{ 'isActive': true, 'expiryDate': { $gte: ?0 } }")
    List<Job> findActiveJobsBeforeExpiry(LocalDateTime date);
}

