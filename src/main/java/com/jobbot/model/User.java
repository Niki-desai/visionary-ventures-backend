package com.jobbot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    @Field("email")
    private String email;
    
    @Field("password_hash")
    private String passwordHash;
    
    @Field("first_name")
    private String firstName;
    
    @Field("last_name")
    private String lastName;
    
    @Field("phone")
    private String phone;
    
    @Field("profile_picture_url")
    private String profilePictureUrl;
    
    @Field("preferences")
    private UserPreferences preferences;
    
    @Field("subscription_tier")
    private SubscriptionTier subscriptionTier;
    
    @Field("is_active")
    private Boolean isActive;
    
    @Field("email_verified")
    private Boolean emailVerified;
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("last_login")
    private LocalDateTime lastLogin;
    
    @Field("otp")
    private String otp;
    
    @Field("otp_expiry")
    private LocalDateTime otpExpiry;
    
    @Field("oauth_provider")
    private String oauthProvider; // "google", "github", "linkedin", etc.
    
    @Field("oauth_id")
    private String oauthId;
    
    @Field("registration_otp_verified")
    private Boolean registrationOtpVerified;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    
    public UserPreferences getPreferences() {
        return preferences;
    }
    
    public void setPreferences(UserPreferences preferences) {
        this.preferences = preferences;
    }
    
    public SubscriptionTier getSubscriptionTier() {
        return subscriptionTier;
    }
    
    public void setSubscriptionTier(SubscriptionTier subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
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
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }
    
    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }
    
    public String getOauthProvider() {
        return oauthProvider;
    }
    
    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }
    
    public String getOauthId() {
        return oauthId;
    }
    
    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }
    
    public Boolean getRegistrationOtpVerified() {
        return registrationOtpVerified;
    }
    
    public void setRegistrationOtpVerified(Boolean registrationOtpVerified) {
        this.registrationOtpVerified = registrationOtpVerified;
    }
    
    // Nested classes
    public static class UserPreferences {
        @Field("job_alerts_enabled")
        private Boolean jobAlertsEnabled;
        
        @Field("notification_email")
        private Boolean notificationEmail;
        
        @Field("notification_sms")
        private Boolean notificationSms;
        
        @Field("auto_apply_enabled")
        private Boolean autoApplyEnabled;
        
        @Field("preferred_locations")
        private List<String> preferredLocations;
        
        @Field("preferred_industries")
        private List<String> preferredIndustries;
        
        @Field("min_salary")
        private Integer minSalary;
        
        @Field("max_salary")
        private Integer maxSalary;
        
        @Field("remote_preference")
        private String remotePreference; // "remote", "hybrid", "onsite", "any"
        
        // Getters and Setters
        public Boolean getJobAlertsEnabled() {
            return jobAlertsEnabled;
        }
        
        public void setJobAlertsEnabled(Boolean jobAlertsEnabled) {
            this.jobAlertsEnabled = jobAlertsEnabled;
        }
        
        public Boolean getNotificationEmail() {
            return notificationEmail;
        }
        
        public void setNotificationEmail(Boolean notificationEmail) {
            this.notificationEmail = notificationEmail;
        }
        
        public Boolean getNotificationSms() {
            return notificationSms;
        }
        
        public void setNotificationSms(Boolean notificationSms) {
            this.notificationSms = notificationSms;
        }
        
        public Boolean getAutoApplyEnabled() {
            return autoApplyEnabled;
        }
        
        public void setAutoApplyEnabled(Boolean autoApplyEnabled) {
            this.autoApplyEnabled = autoApplyEnabled;
        }
        
        public List<String> getPreferredLocations() {
            return preferredLocations;
        }
        
        public void setPreferredLocations(List<String> preferredLocations) {
            this.preferredLocations = preferredLocations;
        }
        
        public List<String> getPreferredIndustries() {
            return preferredIndustries;
        }
        
        public void setPreferredIndustries(List<String> preferredIndustries) {
            this.preferredIndustries = preferredIndustries;
        }
        
        public Integer getMinSalary() {
            return minSalary;
        }
        
        public void setMinSalary(Integer minSalary) {
            this.minSalary = minSalary;
        }
        
        public Integer getMaxSalary() {
            return maxSalary;
        }
        
        public void setMaxSalary(Integer maxSalary) {
            this.maxSalary = maxSalary;
        }
        
        public String getRemotePreference() {
            return remotePreference;
        }
        
        public void setRemotePreference(String remotePreference) {
            this.remotePreference = remotePreference;
        }
    }
    
    public enum SubscriptionTier {
        FREE, BASIC, PREMIUM, ENTERPRISE
    }
}

