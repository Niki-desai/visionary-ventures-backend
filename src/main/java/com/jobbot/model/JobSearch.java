package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "job_searches")
public class JobSearch {
    
    @Id
    private String id;
    
    @Indexed
    @Field("user_id")
    private String userId;
    
    @Field("name")
    private String name; // User-friendly name for the search
    
    @Field("is_active")
    private Boolean isActive;
    
    @Field("search_criteria")
    private SearchCriteria searchCriteria;
    
    @Field("alert_settings")
    private AlertSettings alertSettings;
    
    @Field("last_searched_at")
    private LocalDateTime lastSearchedAt;
    
    @Field("results_count")
    private Integer resultsCount;
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }
    
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
    
    public AlertSettings getAlertSettings() {
        return alertSettings;
    }
    
    public void setAlertSettings(AlertSettings alertSettings) {
        this.alertSettings = alertSettings;
    }
    
    public LocalDateTime getLastSearchedAt() {
        return lastSearchedAt;
    }
    
    public void setLastSearchedAt(LocalDateTime lastSearchedAt) {
        this.lastSearchedAt = lastSearchedAt;
    }
    
    public Integer getResultsCount() {
        return resultsCount;
    }
    
    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
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
    public static class SearchCriteria {
        @Field("keywords")
        private List<String> keywords;
        
        @Field("job_titles")
        private List<String> jobTitles;
        
        @Field("locations")
        private List<String> locations;
        
        @Field("remote_type")
        private String remoteType; // "remote", "hybrid", "onsite", "any"
        
        @Field("job_types")
        private List<String> jobTypes; // "full-time", "part-time", "contract", "internship"
        
        @Field("industries")
        private List<String> industries;
        
        @Field("experience_level")
        private String experienceLevel; // "entry", "mid", "senior", "executive"
        
        @Field("salary_min")
        private Integer salaryMin;
        
        @Field("salary_max")
        private Integer salaryMax;
        
        @Field("required_skills")
        private List<String> requiredSkills;
        
        @Field("company_names")
        private List<String> companyNames;
        
        @Field("exclude_keywords")
        private List<String> excludeKeywords;
        
        // Getters and Setters
        public List<String> getKeywords() {
            return keywords;
        }
        
        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }
        
        public List<String> getJobTitles() {
            return jobTitles;
        }
        
        public void setJobTitles(List<String> jobTitles) {
            this.jobTitles = jobTitles;
        }
        
        public List<String> getLocations() {
            return locations;
        }
        
        public void setLocations(List<String> locations) {
            this.locations = locations;
        }
        
        public String getRemoteType() {
            return remoteType;
        }
        
        public void setRemoteType(String remoteType) {
            this.remoteType = remoteType;
        }
        
        public List<String> getJobTypes() {
            return jobTypes;
        }
        
        public void setJobTypes(List<String> jobTypes) {
            this.jobTypes = jobTypes;
        }
        
        public List<String> getIndustries() {
            return industries;
        }
        
        public void setIndustries(List<String> industries) {
            this.industries = industries;
        }
        
        public String getExperienceLevel() {
            return experienceLevel;
        }
        
        public void setExperienceLevel(String experienceLevel) {
            this.experienceLevel = experienceLevel;
        }
        
        public Integer getSalaryMin() {
            return salaryMin;
        }
        
        public void setSalaryMin(Integer salaryMin) {
            this.salaryMin = salaryMin;
        }
        
        public Integer getSalaryMax() {
            return salaryMax;
        }
        
        public void setSalaryMax(Integer salaryMax) {
            this.salaryMax = salaryMax;
        }
        
        public List<String> getRequiredSkills() {
            return requiredSkills;
        }
        
        public void setRequiredSkills(List<String> requiredSkills) {
            this.requiredSkills = requiredSkills;
        }
        
        public List<String> getCompanyNames() {
            return companyNames;
        }
        
        public void setCompanyNames(List<String> companyNames) {
            this.companyNames = companyNames;
        }
        
        public List<String> getExcludeKeywords() {
            return excludeKeywords;
        }
        
        public void setExcludeKeywords(List<String> excludeKeywords) {
            this.excludeKeywords = excludeKeywords;
        }
    }
    
    public static class AlertSettings {
        @Field("enabled")
        private Boolean enabled;
        
        @Field("frequency")
        private String frequency; // "daily", "weekly", "realtime"
        
        @Field("email_notifications")
        private Boolean emailNotifications;
        
        @Field("sms_notifications")
        private Boolean smsNotifications;
        
        @Field("max_results_per_alert")
        private Integer maxResultsPerAlert;
        
        // Getters and Setters
        public Boolean getEnabled() {
            return enabled;
        }
        
        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getFrequency() {
            return frequency;
        }
        
        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }
        
        public Boolean getEmailNotifications() {
            return emailNotifications;
        }
        
        public void setEmailNotifications(Boolean emailNotifications) {
            this.emailNotifications = emailNotifications;
        }
        
        public Boolean getSmsNotifications() {
            return smsNotifications;
        }
        
        public void setSmsNotifications(Boolean smsNotifications) {
            this.smsNotifications = smsNotifications;
        }
        
        public Integer getMaxResultsPerAlert() {
            return maxResultsPerAlert;
        }
        
        public void setMaxResultsPerAlert(Integer maxResultsPerAlert) {
            this.maxResultsPerAlert = maxResultsPerAlert;
        }
    }
}

