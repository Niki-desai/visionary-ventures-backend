package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "applications")
public class Application {
    
    @Id
    private String id;
    
    @Indexed
    @Field("user_id")
    private String userId;
    
    @Indexed
    @Field("job_id")
    private String jobId;
    
    @Field("resume_id")
    private String resumeId;
    
    @Field("status")
    private ApplicationStatus status;
    
    @Field("cover_letter")
    private String coverLetter;
    
    @Field("cover_letter_generated")
    private Boolean coverLetterGenerated; // AI generated or user written
    
    @Field("application_method")
    private String applicationMethod; // "auto", "manual", "ai_assisted"
    
    @Field("submitted_at")
    private LocalDateTime submittedAt;
    
    @Field("external_application_id")
    private String externalApplicationId;
    
    @Field("tracking_info")
    private TrackingInfo trackingInfo;
    
    @Field("ai_insights")
    private AIInsights aiInsights;
    
    @Field("notes")
    private String notes;
    
    @Field("follow_up_date")
    private LocalDateTime followUpDate;
    
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
    
    public String getJobId() {
        return jobId;
    }
    
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
    public String getResumeId() {
        return resumeId;
    }
    
    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public Boolean getCoverLetterGenerated() {
        return coverLetterGenerated;
    }
    
    public void setCoverLetterGenerated(Boolean coverLetterGenerated) {
        this.coverLetterGenerated = coverLetterGenerated;
    }
    
    public String getApplicationMethod() {
        return applicationMethod;
    }
    
    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public String getExternalApplicationId() {
        return externalApplicationId;
    }
    
    public void setExternalApplicationId(String externalApplicationId) {
        this.externalApplicationId = externalApplicationId;
    }
    
    public TrackingInfo getTrackingInfo() {
        return trackingInfo;
    }
    
    public void setTrackingInfo(TrackingInfo trackingInfo) {
        this.trackingInfo = trackingInfo;
    }
    
    public AIInsights getAiInsights() {
        return aiInsights;
    }
    
    public void setAiInsights(AIInsights aiInsights) {
        this.aiInsights = aiInsights;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getFollowUpDate() {
        return followUpDate;
    }
    
    public void setFollowUpDate(LocalDateTime followUpDate) {
        this.followUpDate = followUpDate;
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
    public static class ApplicationStatus {
        @Field("current")
        private String current; // "draft", "submitted", "under_review", "interview", "offer", "rejected", "withdrawn"
        
        @Field("status_history")
        private List<StatusHistory> statusHistory;
        
        @Field("last_updated")
        private LocalDateTime lastUpdated;
        
        // Getters and Setters
        public String getCurrent() {
            return current;
        }
        
        public void setCurrent(String current) {
            this.current = current;
        }
        
        public List<StatusHistory> getStatusHistory() {
            return statusHistory;
        }
        
        public void setStatusHistory(List<StatusHistory> statusHistory) {
            this.statusHistory = statusHistory;
        }
        
        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }
        
        public void setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
    
    public static class StatusHistory {
        @Field("status")
        private String status;
        
        @Field("changed_at")
        private LocalDateTime changedAt;
        
        @Field("changed_by")
        private String changedBy; // "user", "system", "employer"
        
        @Field("notes")
        private String notes;
        
        // Getters and Setters
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public LocalDateTime getChangedAt() {
            return changedAt;
        }
        
        public void setChangedAt(LocalDateTime changedAt) {
            this.changedAt = changedAt;
        }
        
        public String getChangedBy() {
            return changedBy;
        }
        
        public void setChangedBy(String changedBy) {
            this.changedBy = changedBy;
        }
        
        public String getNotes() {
            return notes;
        }
        
        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
    
    public static class TrackingInfo {
        @Field("email_tracking_enabled")
        private Boolean emailTrackingEnabled;
        
        @Field("last_email_opened")
        private LocalDateTime lastEmailOpened;
        
        @Field("email_open_count")
        private Integer emailOpenCount;
        
        @Field("profile_viewed")
        private Boolean profileViewed;
        
        @Field("profile_viewed_at")
        private LocalDateTime profileViewedAt;
        
        @Field("application_url")
        private String applicationUrl;
        
        // Getters and Setters
        public Boolean getEmailTrackingEnabled() {
            return emailTrackingEnabled;
        }
        
        public void setEmailTrackingEnabled(Boolean emailTrackingEnabled) {
            this.emailTrackingEnabled = emailTrackingEnabled;
        }
        
        public LocalDateTime getLastEmailOpened() {
            return lastEmailOpened;
        }
        
        public void setLastEmailOpened(LocalDateTime lastEmailOpened) {
            this.lastEmailOpened = lastEmailOpened;
        }
        
        public Integer getEmailOpenCount() {
            return emailOpenCount;
        }
        
        public void setEmailOpenCount(Integer emailOpenCount) {
            this.emailOpenCount = emailOpenCount;
        }
        
        public Boolean getProfileViewed() {
            return profileViewed;
        }
        
        public void setProfileViewed(Boolean profileViewed) {
            this.profileViewed = profileViewed;
        }
        
        public LocalDateTime getProfileViewedAt() {
            return profileViewedAt;
        }
        
        public void setProfileViewedAt(LocalDateTime profileViewedAt) {
            this.profileViewedAt = profileViewedAt;
        }
        
        public String getApplicationUrl() {
            return applicationUrl;
        }
        
        public void setApplicationUrl(String applicationUrl) {
            this.applicationUrl = applicationUrl;
        }
    }
    
    public static class AIInsights {
        @Field("match_score")
        private Double matchScore;
        
        @Field("strengths")
        private List<String> strengths;
        
        @Field("weaknesses")
        private List<String> weaknesses;
        
        @Field("suggestions")
        private List<String> suggestions;
        
        @Field("cover_letter_quality_score")
        private Double coverLetterQualityScore;
        
        @Field("analyzed_at")
        private LocalDateTime analyzedAt;
        
        // Getters and Setters
        public Double getMatchScore() {
            return matchScore;
        }
        
        public void setMatchScore(Double matchScore) {
            this.matchScore = matchScore;
        }
        
        public List<String> getStrengths() {
            return strengths;
        }
        
        public void setStrengths(List<String> strengths) {
            this.strengths = strengths;
        }
        
        public List<String> getWeaknesses() {
            return weaknesses;
        }
        
        public void setWeaknesses(List<String> weaknesses) {
            this.weaknesses = weaknesses;
        }
        
        public List<String> getSuggestions() {
            return suggestions;
        }
        
        public void setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }
        
        public Double getCoverLetterQualityScore() {
            return coverLetterQualityScore;
        }
        
        public void setCoverLetterQualityScore(Double coverLetterQualityScore) {
            this.coverLetterQualityScore = coverLetterQualityScore;
        }
        
        public LocalDateTime getAnalyzedAt() {
            return analyzedAt;
        }
        
        public void setAnalyzedAt(LocalDateTime analyzedAt) {
            this.analyzedAt = analyzedAt;
        }
    }
}

