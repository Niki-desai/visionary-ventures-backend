package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "resumes")
public class Resume {
    
    @Id
    private String id;
    
    @Indexed
    @Field("user_id")
    private String userId;
    
    @Field("title")
    private String title;
    
    @Field("is_default")
    private Boolean isDefault;
    
    @Field("personal_info")
    private PersonalInfo personalInfo;
    
    @Field("summary")
    private String summary;
    
    @Field("experience")
    private List<Experience> experience;
    
    @Field("education")
    private List<Education> education;
    
    @Field("skills")
    private List<String> skills;
    
    @Field("certifications")
    private List<Certification> certifications;
    
    @Field("languages")
    private List<Language> languages;
    
    @Field("projects")
    private List<Project> projects;
    
    @Field("file_url")
    private String fileUrl; // PDF/DOCX file storage URL
    
    @Field("file_format")
    private String fileFormat; // "pdf", "docx", "json"
    
    @Field("ai_enhanced")
    private Boolean aiEnhanced; // Whether AI has enhanced this resume
    
    @Field("version")
    private Integer version;
    
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }
    
    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public List<Experience> getExperience() {
        return experience;
    }
    
    public void setExperience(List<Experience> experience) {
        this.experience = experience;
    }
    
    public List<Education> getEducation() {
        return education;
    }
    
    public void setEducation(List<Education> education) {
        this.education = education;
    }
    
    public List<String> getSkills() {
        return skills;
    }
    
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
    
    public List<Certification> getCertifications() {
        return certifications;
    }
    
    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }
    
    public List<Language> getLanguages() {
        return languages;
    }
    
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
    
    public List<Project> getProjects() {
        return projects;
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public String getFileFormat() {
        return fileFormat;
    }
    
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    
    public Boolean getAiEnhanced() {
        return aiEnhanced;
    }
    
    public void setAiEnhanced(Boolean aiEnhanced) {
        this.aiEnhanced = aiEnhanced;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
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
    public static class PersonalInfo {
        @Field("full_name")
        private String fullName;
        
        @Field("email")
        private String email;
        
        @Field("phone")
        private String phone;
        
        @Field("address")
        private String address;
        
        @Field("linkedin_url")
        private String linkedinUrl;
        
        @Field("github_url")
        private String githubUrl;
        
        @Field("portfolio_url")
        private String portfolioUrl;
        
        // Getters and Setters
        public String getFullName() {
            return fullName;
        }
        
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public String getLinkedinUrl() {
            return linkedinUrl;
        }
        
        public void setLinkedinUrl(String linkedinUrl) {
            this.linkedinUrl = linkedinUrl;
        }
        
        public String getGithubUrl() {
            return githubUrl;
        }
        
        public void setGithubUrl(String githubUrl) {
            this.githubUrl = githubUrl;
        }
        
        public String getPortfolioUrl() {
            return portfolioUrl;
        }
        
        public void setPortfolioUrl(String portfolioUrl) {
            this.portfolioUrl = portfolioUrl;
        }
    }
    
    public static class Experience {
        @Field("company")
        private String company;
        
        @Field("position")
        private String position;
        
        @Field("start_date")
        private String startDate;
        
        @Field("end_date")
        private String endDate; // "Present" for current job
        
        @Field("is_current")
        private Boolean isCurrent;
        
        @Field("description")
        private String description;
        
        @Field("achievements")
        private List<String> achievements;
        
        // Getters and Setters
        public String getCompany() {
            return company;
        }
        
        public void setCompany(String company) {
            this.company = company;
        }
        
        public String getPosition() {
            return position;
        }
        
        public void setPosition(String position) {
            this.position = position;
        }
        
        public String getStartDate() {
            return startDate;
        }
        
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
        
        public String getEndDate() {
            return endDate;
        }
        
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
        
        public Boolean getIsCurrent() {
            return isCurrent;
        }
        
        public void setIsCurrent(Boolean isCurrent) {
            this.isCurrent = isCurrent;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public List<String> getAchievements() {
            return achievements;
        }
        
        public void setAchievements(List<String> achievements) {
            this.achievements = achievements;
        }
    }
    
    public static class Education {
        @Field("institution")
        private String institution;
        
        @Field("degree")
        private String degree;
        
        @Field("field_of_study")
        private String fieldOfStudy;
        
        @Field("start_date")
        private String startDate;
        
        @Field("end_date")
        private String endDate;
        
        @Field("gpa")
        private String gpa;
        
        @Field("honors")
        private List<String> honors;
        
        // Getters and Setters
        public String getInstitution() {
            return institution;
        }
        
        public void setInstitution(String institution) {
            this.institution = institution;
        }
        
        public String getDegree() {
            return degree;
        }
        
        public void setDegree(String degree) {
            this.degree = degree;
        }
        
        public String getFieldOfStudy() {
            return fieldOfStudy;
        }
        
        public void setFieldOfStudy(String fieldOfStudy) {
            this.fieldOfStudy = fieldOfStudy;
        }
        
        public String getStartDate() {
            return startDate;
        }
        
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
        
        public String getEndDate() {
            return endDate;
        }
        
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
        
        public String getGpa() {
            return gpa;
        }
        
        public void setGpa(String gpa) {
            this.gpa = gpa;
        }
        
        public List<String> getHonors() {
            return honors;
        }
        
        public void setHonors(List<String> honors) {
            this.honors = honors;
        }
    }
    
    public static class Certification {
        @Field("name")
        private String name;
        
        @Field("issuing_organization")
        private String issuingOrganization;
        
        @Field("issue_date")
        private String issueDate;
        
        @Field("expiry_date")
        private String expiryDate;
        
        @Field("credential_id")
        private String credentialId;
        
        @Field("credential_url")
        private String credentialUrl;
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getIssuingOrganization() {
            return issuingOrganization;
        }
        
        public void setIssuingOrganization(String issuingOrganization) {
            this.issuingOrganization = issuingOrganization;
        }
        
        public String getIssueDate() {
            return issueDate;
        }
        
        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }
        
        public String getExpiryDate() {
            return expiryDate;
        }
        
        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }
        
        public String getCredentialId() {
            return credentialId;
        }
        
        public void setCredentialId(String credentialId) {
            this.credentialId = credentialId;
        }
        
        public String getCredentialUrl() {
            return credentialUrl;
        }
        
        public void setCredentialUrl(String credentialUrl) {
            this.credentialUrl = credentialUrl;
        }
    }
    
    public static class Language {
        @Field("name")
        private String name;
        
        @Field("proficiency")
        private String proficiency; // "native", "fluent", "conversational", "basic"
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getProficiency() {
            return proficiency;
        }
        
        public void setProficiency(String proficiency) {
            this.proficiency = proficiency;
        }
    }
    
    public static class Project {
        @Field("name")
        private String name;
        
        @Field("description")
        private String description;
        
        @Field("technologies")
        private List<String> technologies;
        
        @Field("url")
        private String url;
        
        @Field("start_date")
        private String startDate;
        
        @Field("end_date")
        private String endDate;
        
        // Getters and Setters
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public List<String> getTechnologies() {
            return technologies;
        }
        
        public void setTechnologies(List<String> technologies) {
            this.technologies = technologies;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getStartDate() {
            return startDate;
        }
        
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
        
        public String getEndDate() {
            return endDate;
        }
        
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
}

