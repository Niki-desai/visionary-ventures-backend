package com.jobbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Component
public class MongoConnectionChecker {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void checkConnection() {
        try {
            // Test MongoDB connection
            String databaseName = mongoTemplate.getDb().getName();
            System.out.println("🔌 Connected to MongoDB database: " + databaseName);
            
            // List existing collections
            Set<String> collections = mongoTemplate.getCollectionNames();
            System.out.println("📚 Existing collections: " + collections);
            
            if (collections.isEmpty()) {
                System.out.println("ℹ️  No collections found. Collections will be created when data is inserted.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection failed: " + e.getMessage());
            System.err.println("⚠️  Please ensure MongoDB is running on mongodb://localhost:27017");
            e.printStackTrace();
        }
    }
}

