package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "jobs")
public class Job {
    
    @Id
    private String id;
    
    @Indexed
    @Field("title")
    private String title;
    
    @Field("description")
    private String description;
    
    @Field("company_name")
    private String companyName;
    
    @Indexed
    @Field("company_id")
    private String companyId;
    
    @Field("location")
    private JobLocation location;
    
    @Indexed
    @Field("job_type")
    private String jobType; // "full-time", "part-time", "contract", "internship"
    
    @Field("remote_type")
    private String remoteType; // "remote", "hybrid", "onsite"
    
    @Field("salary")
    private SalaryRange salary;
    
    @Indexed
    @Field("industry")
    private String industry;
    
    @Field("required_skills")
    private List<String> requiredSkills;
    
    @Field("preferred_skills")
    private List<String> preferredSkills;
    
    @Field("experience_level")
    private String experienceLevel; // "entry", "mid", "senior", "executive"
    
    @Field("education_requirements")
    private List<String> educationRequirements;
    
    @Field("source")
    private JobSource source;
    
    @Field("external_id")
    private String externalId; // ID from external job board
    
    @Field("external_url")
    private String externalUrl;
    
    @Field("application_url")
    private String applicationUrl;
    
    @Field("posted_date")
    private LocalDateTime postedDate;
    
    @Field("expiry_date")
    private LocalDateTime expiryDate;
    
    @Field("is_active")
    private Boolean isActive;
    
    @Field("application_count")
    private Integer applicationCount;
    
    @Field("ai_analysis")
    private AIAnalysis aiAnalysis;
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    public JobLocation getLocation() {
        return location;
    }
    
    public void setLocation(JobLocation location) {
        this.location = location;
    }
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    public String getRemoteType() {
        return remoteType;
    }
    
    public void setRemoteType(String remoteType) {
        this.remoteType = remoteType;
    }
    
    public SalaryRange getSalary() {
        return salary;
    }
    
    public void setSalary(SalaryRange salary) {
        this.salary = salary;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public List<String> getRequiredSkills() {
        return requiredSkills;
    }
    
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
    
    public List<String> getPreferredSkills() {
        return preferredSkills;
    }
    
    public void setPreferredSkills(List<String> preferredSkills) {
        this.preferredSkills = preferredSkills;
    }
    
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    
    public List<String> getEducationRequirements() {
        return educationRequirements;
    }
    
    public void setEducationRequirements(List<String> educationRequirements) {
        this.educationRequirements = educationRequirements;
    }
    
    public JobSource getSource() {
        return source;
    }
    
    public void setSource(JobSource source) {
        this.source = source;
    }
    
    public String getExternalId() {
        return externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    
    public String getExternalUrl() {
        return externalUrl;
    }
    
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
    
    public String getApplicationUrl() {
        return applicationUrl;
    }
    
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
    
    public LocalDateTime getPostedDate() {
        return postedDate;
    }
    
    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }
    
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getApplicationCount() {
        return applicationCount;
    }
    
    public void setApplicationCount(Integer applicationCount) {
        this.applicationCount = applicationCount;
    }
    
    public AIAnalysis getAiAnalysis() {
        return aiAnalysis;
    }
    
    public void setAiAnalysis(AIAnalysis aiAnalysis) {
        this.aiAnalysis = aiAnalysis;
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
    public static class JobLocation {
        @Field("city")
        private String city;
        
        @Field("state")
        private String state;
        
        @Field("country")
        private String country;
        
        @Field("zip_code")
        private String zipCode;
        
        @Field("coordinates")
        private Coordinates coordinates;
        
        // Getters and Setters
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getState() {
            return state;
        }
        
        public void setState(String state) {
            this.state = state;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public String getZipCode() {
            return zipCode;
        }
        
        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
        
        public Coordinates getCoordinates() {
            return coordinates;
        }
        
        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }
    }
    
    public static class Coordinates {
        @Field("latitude")
        private Double latitude;
        
        @Field("longitude")
        private Double longitude;
        
        // Getters and Setters
        public Double getLatitude() {
            return latitude;
        }
        
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        
        public Double getLongitude() {
            return longitude;
        }
        
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
    
    public static class SalaryRange {
        @Field("min")
        private Integer min;
        
        @Field("max")
        private Integer max;
        
        @Field("currency")
        private String currency;
        
        @Field("period")
        private String period; // "hourly", "monthly", "yearly"
        
        // Getters and Setters
        public Integer getMin() {
            return min;
        }
        
        public void setMin(Integer min) {
            this.min = min;
        }
        
        public Integer getMax() {
            return max;
        }
        
        public void setMax(Integer max) {
            this.max = max;
        }
        
        public String getCurrency() {
            return currency;
        }
        
        public void setCurrency(String currency) {
            this.currency = currency;
        }
        
        public String getPeriod() {
            return period;
        }
        
        public void setPeriod(String period) {
            this.period = period;
        }
    }
    
    public static class JobSource {
        @Field("name")
        private String name; // "linkedin", "indeed", "glassdoor", "custom"
        
        @Field("api_provider")
        private String apiProvider;
        
        @Field("scraped")
        private Boolean scraped;
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getApiProvider() {
            return apiProvider;
        }
        
        public void setApiProvider(String apiProvider) {
            this.apiProvider = apiProvider;
        }
        
        public Boolean getScraped() {
            return scraped;
        }
        
        public void setScraped(Boolean scraped) {
            this.scraped = scraped;
        }
    }
    
    public static class AIAnalysis {
        @Field("match_score")
        private Double matchScore; // 0.0 to 1.0
        
        @Field("skill_match_percentage")
        private Double skillMatchPercentage;
        
        @Field("recommended")
        private Boolean recommended;
        
        @Field("reasoning")
        private String reasoning;
        
        @Field("extracted_requirements")
        private Map<String, Object> extractedRequirements;
        
        @Field("analyzed_at")
        private LocalDateTime analyzedAt;
        
        // Getters and Setters
        public Double getMatchScore() {
            return matchScore;
        }
        
        public void setMatchScore(Double matchScore) {
            this.matchScore = matchScore;
        }
        
        public Double getSkillMatchPercentage() {
            return skillMatchPercentage;
        }
        
        public void setSkillMatchPercentage(Double skillMatchPercentage) {
            this.skillMatchPercentage = skillMatchPercentage;
        }
        
        public Boolean getRecommended() {
            return recommended;
        }
        
        public void setRecommended(Boolean recommended) {
            this.recommended = recommended;
        }
        
        public String getReasoning() {
            return reasoning;
        }
        
        public void setReasoning(String reasoning) {
            this.reasoning = reasoning;
        }
        
        public Map<String, Object> getExtractedRequirements() {
            return extractedRequirements;
        }
        
        public void setExtractedRequirements(Map<String, Object> extractedRequirements) {
            this.extractedRequirements = extractedRequirements;
        }
        
        public LocalDateTime getAnalyzedAt() {
            return analyzedAt;
        }
        
        public void setAnalyzedAt(LocalDateTime analyzedAt) {
            this.analyzedAt = analyzedAt;
        }
    }
}

