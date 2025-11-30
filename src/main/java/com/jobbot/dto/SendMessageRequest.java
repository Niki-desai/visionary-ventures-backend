package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

@Schema(description = "Request to send a message in a chat")
public class SendMessageRequest {
    
    @Schema(description = "Chat ID", example = "507f1f77bcf86cd799439011")
    @NotBlank(message = "Chat ID is required")
    private String chatId;
    
    @Schema(description = "Message content", example = "Can you help me find a job?")
    @NotBlank(message = "Message content is required")
    private String content;
    
    @Schema(description = "Message type", example = "text", allowableValues = {"text", "image", "file", "code", "markdown"})
    private String messageType;
    
    @Schema(description = "Parent message ID for threading", example = "507f1f77bcf86cd799439012")
    private String parentMessageId;
    
    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;
    
    public SendMessageRequest() {}
    
    public String getChatId() {
        return chatId;
    }
    
    public void setChatId(String chatId) {
        this.chatId = chatId;
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
}

