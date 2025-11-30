package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "chat_messages")
public class ChatMessage {
    
    @Id
    private String id;
    
    @Indexed
    @Field("chat_id")
    private String chatId; // Reference to Chat
    
    @Indexed
    @Field("user_id")
    private String userId; // For quick user queries
    
    @Field("role")
    private String role; // "user", "assistant", "system"
    
    @Field("content")
    private String content;
    
    @Field("message_type")
    private String messageType; // "text", "image", "file", "code", "markdown"
    
    @Field("tokens_used")
    private Integer tokensUsed; // For AI responses
    
    @Field("model_used")
    private String modelUsed; // AI model name (e.g., "gpt-4", "claude-3")
    
    @Field("is_edited")
    private Boolean isEdited;
    
    @Field("edited_at")
    private LocalDateTime editedAt;
    
    @Field("parent_message_id")
    private String parentMessageId; // For threaded conversations
    
    @Field("metadata")
    private Map<String, Object> metadata; // Additional data (attachments, formatting, etc.)
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getChatId() {
        return chatId;
    }
    
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
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
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
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
    
    public Boolean getIsEdited() {
        return isEdited;
    }
    
    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }
    
    public LocalDateTime getEditedAt() {
        return editedAt;
    }
    
    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }
    
    public String getParentMessageId() {
        return parentMessageId;
    }
    
    public void setParentMessageId(String parentMessageId) {
        this.parentMessageId = parentMessageId;
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
}

