package com.jobbot.service;

import com.jobbot.dto.*;
import com.jobbot.model.User;
import com.jobbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTService jwtService;
    
    @Autowired
    private OTPService otpService;
    
    @Autowired
    private EmailService emailService;
    
    // Register - Step 1: Create user and send OTP
    public ApiResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(false); // Not active until OTP verified
        user.setEmailVerified(false);
        user.setRegistrationOtpVerified(false);
        user.setSubscriptionTier(User.SubscriptionTier.FREE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Generate and send OTP
        String otp = otpService.generateOTP();
        otpService.storeOTP(request.getEmail(), otp, OTPService.OTPType.REGISTRATION);
        emailService.sendRegistrationOTP(request.getEmail(), otp);
        
        // Save user
        userRepository.save(user);
        
        return ApiResponse.success("Registration successful. OTP sent to your email. Please verify to complete registration.");
    }
    
    // Register - Step 2: Verify OTP and complete registration
    public ApiResponse verifyRegistrationOTP(OTPRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ApiResponse.error("User not found");
        }
        
        User user = userOpt.get();
        
        // Verify OTP
        if (!otpService.verifyOTP(request.getEmail(), request.getOtp(), OTPService.OTPType.REGISTRATION)) {
            return ApiResponse.error("Invalid or expired OTP");
        }
        
        // Activate user
        user.setEmailVerified(true);
        user.setIsActive(true);
        user.setRegistrationOtpVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate token
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail(), 
                user.getFirstName(), user.getLastName());
        authResponse.setMessage("Registration completed successfully");
        
        return ApiResponse.success("Registration verified successfully", authResponse);
    }
    
    // Login with email and password
    public ApiResponse login(AuthRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ApiResponse.error("Invalid email or password");
        }
        
        User user = userOpt.get();
        
        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ApiResponse.error("Invalid email or password");
        }
        
        // Check if user is active
        if (Boolean.FALSE.equals(user.getIsActive())) {
            return ApiResponse.error("Account not activated. Please verify your email.");
        }
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate token
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail(), 
                user.getFirstName(), user.getLastName());
        authResponse.setMessage("Login successful");
        
        return ApiResponse.success("Login successful", authResponse);
    }
    
    // OAuth Login/Register
    public ApiResponse oauthLogin(String provider, String oauthId, String email, String firstName, String lastName) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        User user;
        boolean isNewUser = false;
        
        if (userOpt.isPresent()) {
            // Existing user - OAuth login
            user = userOpt.get();
            // Update OAuth info if not set
            if (user.getOauthProvider() == null) {
                user.setOauthProvider(provider);
                user.setOauthId(oauthId);
            }
        } else {
            // New user - OAuth register
            isNewUser = true;
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
            user.setEmailVerified(true); // OAuth emails are pre-verified
            user.setIsActive(true);
            user.setRegistrationOtpVerified(true);
            user.setSubscriptionTier(User.SubscriptionTier.FREE);
            user.setCreatedAt(LocalDateTime.now());
        }
        
        user.setLastLogin(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Generate token
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        AuthResponse authResponse = new AuthResponse(token, user.getId(), user.getEmail(), 
                user.getFirstName(), user.getLastName());
        authResponse.setMessage(isNewUser ? "Registration successful" : "Login successful");
        
        return ApiResponse.success(isNewUser ? "Registration successful" : "Login successful", authResponse);
    }
    
    // Forgot Password - Step 1: Send OTP
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists for security
            return ApiResponse.success("If the email exists, an OTP has been sent.");
        }
        
        User user = userOpt.get();
        if (Boolean.FALSE.equals(user.getIsActive())) {
            return ApiResponse.error("Account not activated");
        }
        
        // Generate and send OTP
        String otp = otpService.generateOTP();
        otpService.storeOTP(request.getEmail(), otp, OTPService.OTPType.FORGOT_PASSWORD);
        emailService.sendForgotPasswordOTP(request.getEmail(), otp);
        
        return ApiResponse.success("OTP sent to your email. Please check and verify.");
    }
    
    // Forgot Password - Step 2: Verify OTP
    public ApiResponse verifyForgotPasswordOTP(OTPRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ApiResponse.error("User not found");
        }
        
        // Verify OTP
        if (!otpService.verifyOTP(request.getEmail(), request.getOtp(), OTPService.OTPType.FORGOT_PASSWORD)) {
            return ApiResponse.error("Invalid or expired OTP");
        }
        
        // Store verified OTP for password reset (in production, use a separate token)
        otpService.storeOTP(request.getEmail(), request.getOtp(), OTPService.OTPType.FORGOT_PASSWORD);
        
        return ApiResponse.success("OTP verified successfully. You can now reset your password.");
    }
    
    // Forgot Password - Step 3: Reset password
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ApiResponse.error("User not found");
        }
        
        User user = userOpt.get();
        
        // Verify OTP again
        if (!otpService.verifyOTP(request.getEmail(), request.getOtp(), OTPService.OTPType.FORGOT_PASSWORD)) {
            return ApiResponse.error("Invalid or expired OTP");
        }
        
        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return ApiResponse.success("Password reset successfully. You can now login with your new password.");
    }
}

