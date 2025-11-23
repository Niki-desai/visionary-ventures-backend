package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "OAuth authentication request")
public class OAuthRequest {
    
    @NotBlank(message = "OAuth ID is required")
    @Schema(description = "OAuth provider user ID", example = "123456789", required = true)
    private String oauthId;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email from OAuth provider", example = "user@gmail.com", required = true)
    private String email;
    
    @NotBlank(message = "First name is required")
    @Schema(description = "User's first name from OAuth provider", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "User's last name from OAuth provider", example = "Doe")
    private String lastName;
    
    public OAuthRequest() {}
    
    public OAuthRequest(String oauthId, String email, String firstName, String lastName) {
        this.oauthId = oauthId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getOauthId() {
        return oauthId;
    }
    
    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
}

