package com.jobbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;

import jakarta.annotation.PostConstruct;

@Configuration
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void createIndexes() {
        System.out.println("📊 Creating MongoDB indexes...");
        try {
            createUserIndexes();
            System.out.println("✅ User indexes created");
            createJobIndexes();
            System.out.println("✅ Job indexes created");
            createApplicationIndexes();
            System.out.println("✅ Application indexes created");
            createResumeIndexes();
            System.out.println("✅ Resume indexes created");
            createJobSearchIndexes();
            System.out.println("✅ JobSearch indexes created");
            createAIConversationIndexes();
            System.out.println("✅ AIConversation indexes created");
            createChatIndexes();
            System.out.println("✅ Chat indexes created");
            createChatMessageIndexes();
            System.out.println("✅ ChatMessage indexes created");
            System.out.println("🎉 All MongoDB indexes created successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error creating indexes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createUserIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("users");
        
        // Unique email index
        indexOps.ensureIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique());
    }

    private void createJobIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("jobs");
        
        // Text search index for title and description
        TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
                .onField("title")
                .onField("description")
                .build();
        indexOps.ensureIndex(textIndex);
        
        // Single field indexes
        indexOps.ensureIndex(new Index().on("companyId", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("jobType", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("industry", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("remoteType", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("isActive", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("expiryDate", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("requiredSkills", org.springframework.data.domain.Sort.Direction.ASC));
        
        // Compound index for active jobs
        indexOps.ensureIndex(new Index().on("isActive", org.springframework.data.domain.Sort.Direction.ASC)
                .on("expiryDate", org.springframework.data.domain.Sort.Direction.ASC));
    }

    private void createApplicationIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("applications");
        
        // Compound indexes
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("jobId", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("status.current", org.springframework.data.domain.Sort.Direction.ASC));
        
        // Single field indexes
        indexOps.ensureIndex(new Index().on("jobId", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("submittedAt", org.springframework.data.domain.Sort.Direction.DESC));
    }

    private void createResumeIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("resumes");
        
        // Compound index for default resume
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("isDefault", org.springframework.data.domain.Sort.Direction.ASC));
    }

    private void createJobSearchIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("job_searches");
        
        // Compound index for active searches
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("isActive", org.springframework.data.domain.Sort.Direction.ASC));
    }

    private void createAIConversationIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("ai_conversations");
        
        // Compound indexes
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("conversationType", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("userId", org.springframework.data.domain.Sort.Direction.ASC)
                .on("isActive", org.springframework.data.domain.Sort.Direction.ASC));
    }
    
    private void createChatIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("chats");
        
        // Single field indexes
        indexOps.ensureIndex(new Index().on("user_id", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("is_active", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("is_pinned", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("last_message_at", org.springframework.data.domain.Sort.Direction.DESC));
        indexOps.ensureIndex(new Index().on("chat_type", org.springframework.data.domain.Sort.Direction.ASC));
        
        // Compound indexes
        indexOps.ensureIndex(new Index().on("user_id", org.springframework.data.domain.Sort.Direction.ASC)
                .on("is_active", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("user_id", org.springframework.data.domain.Sort.Direction.ASC)
                .on("is_pinned", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("user_id", org.springframework.data.domain.Sort.Direction.ASC)
                .on("last_message_at", org.springframework.data.domain.Sort.Direction.DESC));
        
        // Text index for title search
        TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
                .onField("title")
                .build();
        indexOps.ensureIndex(textIndex);
    }
    
    private void createChatMessageIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps("chat_messages");
        
        // Single field indexes
        indexOps.ensureIndex(new Index().on("chat_id", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("user_id", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("role", org.springframework.data.domain.Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("created_at", org.springframework.data.domain.Sort.Direction.DESC));
        indexOps.ensureIndex(new Index().on("parent_message_id", org.springframework.data.domain.Sort.Direction.ASC));
        
        // Compound indexes
        indexOps.ensureIndex(new Index().on("chat_id", org.springframework.data.domain.Sort.Direction.ASC)
                .on("created_at", org.springframework.data.domain.Sort.Direction.DESC));
        indexOps.ensureIndex(new Index().on("chat_id", org.springframework.data.domain.Sort.Direction.ASC)
                .on("role", org.springframework.data.domain.Sort.Direction.ASC));
    }
}

