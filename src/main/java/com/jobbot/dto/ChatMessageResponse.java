package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Chat message response")
public class ChatMessageResponse {
    
    @Schema(description = "Message ID")
    private String id;
    
    @Schema(description = "Chat ID")
    private String chatId;
    
    @Schema(description = "User ID")
    private String userId;
    
    @Schema(description = "Message role", example = "user")
    private String role;
    
    @Schema(description = "Message content")
    private String content;
    
    @Schema(description = "Message type", example = "text")
    private String messageType;
    
    @Schema(description = "Tokens used (for AI responses)")
    private Integer tokensUsed;
    
    @Schema(description = "Model used (for AI responses)")
    private String modelUsed;
    
    @Schema(description = "Is edited")
    private Boolean isEdited;
    
    @Schema(description = "Edited at")
    private LocalDateTime editedAt;
    
    @Schema(description = "Parent message ID")
    private String parentMessageId;
    
    @Schema(description = "Metadata")
    private Map<String, Object> metadata;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    public ChatMessageResponse() {}
    
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

