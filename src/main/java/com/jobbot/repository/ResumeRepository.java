package com.jobbot.repository;

import com.jobbot.model.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {
    List<Resume> findByUserId(String userId);
    Optional<Resume> findByUserIdAndIsDefaultTrue(String userId);
    List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);
}

