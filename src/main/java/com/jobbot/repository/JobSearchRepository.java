package com.jobbot.repository;

import com.jobbot.model.JobSearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSearchRepository extends MongoRepository<JobSearch, String> {
    List<JobSearch> findByUserId(String userId);
    List<JobSearch> findByUserIdAndIsActiveTrue(String userId);
}

