package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "ai_conversations")
public class AIConversation {
    
    @Id
    private String id;
    
    @Indexed
    @Field("user_id")
    private String userId;
    
    @Field("conversation_type")
    private String conversationType; // "job_search", "resume_review", "cover_letter", "interview_prep", "general"
    
    @Field("context")
    private ConversationContext context;
    
    @Field("messages")
    private List<Message> messages;
    
    @Field("summary")
    private String summary;
    
    @Field("is_active")
    private Boolean isActive;
    
    @Field("metadata")
    private Map<String, Object> metadata;
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getConversationType() {
        return conversationType;
    }
    
    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }
    
    public ConversationContext getContext() {
        return context;
    }
    
    public void setContext(ConversationContext context) {
        this.context = context;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Nested classes
    public static class ConversationContext {
        @Field("job_id")
        private String jobId;
        
        @Field("application_id")
        private String applicationId;
        
        @Field("resume_id")
        private String resumeId;
        
        @Field("search_id")
        private String searchId;
        
        @Field("user_preferences")
        private Map<String, Object> userPreferences;
        
        @Field("session_data")
        private Map<String, Object> sessionData;
        
        // Getters and Setters
        public String getJobId() {
            return jobId;
        }
        
        public void setJobId(String jobId) {
            this.jobId = jobId;
        }
        
        public String getApplicationId() {
            return applicationId;
        }
        
        public void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }
        
        public String getResumeId() {
            return resumeId;
        }
        
        public void setResumeId(String resumeId) {
            this.resumeId = resumeId;
        }
        
        public String getSearchId() {
            return searchId;
        }
        
        public void setSearchId(String searchId) {
            this.searchId = searchId;
        }
        
        public Map<String, Object> getUserPreferences() {
            return userPreferences;
        }
        
        public void setUserPreferences(Map<String, Object> userPreferences) {
            this.userPreferences = userPreferences;
        }
        
        public Map<String, Object> getSessionData() {
            return sessionData;
        }
        
        public void setSessionData(Map<String, Object> sessionData) {
            this.sessionData = sessionData;
        }
    }
    
    public static class Message {
        @Field("role")
        private String role; // "user", "assistant", "system"
        
        @Field("content")
        private String content;
        
        @Field("timestamp")
        private LocalDateTime timestamp;
        
        @Field("tokens_used")
        private Integer tokensUsed;
        
        @Field("model_used")
        private String modelUsed;
        
        @Field("metadata")
        private Map<String, Object> metadata;
        
        // Getters and Setters
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        
        public Integer getTokensUsed() {
            return tokensUsed;
        }
        
        public void setTokensUsed(Integer tokensUsed) {
            this.tokensUsed = tokensUsed;
        }
        
        public String getModelUsed() {
            return modelUsed;
        }
        
        public void setModelUsed(String modelUsed) {
            this.modelUsed = modelUsed;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }
}

