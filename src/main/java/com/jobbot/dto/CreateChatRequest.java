package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

@Schema(description = "Request to create a new chat")
public class CreateChatRequest {
    
    @Schema(description = "Chat title", example = "Job Search Help")
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Chat type", example = "general", allowableValues = {"general", "job_search", "resume_review", "cover_letter", "interview_prep"})
    private String chatType;
    
    @Schema(description = "Additional metadata (jobId, applicationId, etc.)")
    private Map<String, Object> metadata;
    
    public CreateChatRequest() {}
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getChatType() {
        return chatType;
    }
    
    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

