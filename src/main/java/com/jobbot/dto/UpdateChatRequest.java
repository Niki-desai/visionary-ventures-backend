package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update chat details")
public class UpdateChatRequest {
    
    @Schema(description = "Chat title", example = "Updated Chat Title")
    private String title;
    
    @Schema(description = "Pin status", example = "true")
    private Boolean isPinned;
    
    @Schema(description = "Active status", example = "true")
    private Boolean isActive;
    
    public UpdateChatRequest() {}
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Boolean getIsPinned() {
        return isPinned;
    }
    
    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

