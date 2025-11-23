package com.jobbot.controller;

import com.jobbot.dto.*;
import com.jobbot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    @Operation(
            summary = "Register User",
            description = "Register a new user with email and password. OTP will be sent to email for verification."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration initiated, OTP sent"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-otp")
    @Operation(
            summary = "Verify Registration OTP",
            description = "Verify the OTP sent during registration to complete the registration process."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verified, registration completed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponse> verifyRegistrationOTP(@Valid @RequestBody OTPRequest request) {
        ApiResponse response = authService.verifyRegistrationOTP(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login with email and password. Returns JWT token on success."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
        ApiResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/oauth/{provider}")
    @Operation(
            summary = "OAuth Login/Register",
            description = "Login or register using OAuth provider (Google, GitHub, LinkedIn, etc.)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OAuth authentication successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OAuth data")
    })
    public ResponseEntity<ApiResponse> oauthLogin(
            @PathVariable String provider,
            @RequestBody OAuthRequest request) {
        ApiResponse response = authService.oauthLogin(
                provider,
                request.getOauthId(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot Password - Send OTP",
            description = "Request password reset. OTP will be sent to registered email."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP sent to email"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid email")
    })
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ApiResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/verify-forgot-password-otp")
    @Operation(
            summary = "Verify Forgot Password OTP",
            description = "Verify the OTP sent for password reset."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verified successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponse> verifyForgotPasswordOTP(@Valid @RequestBody OTPRequest request) {
        ApiResponse response = authService.verifyForgotPasswordOTP(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset Password",
            description = "Reset password using verified OTP and new password."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP or password")
    })
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ApiResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}

