package com.jobbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username:noreply@jobbot.com}")
    private String fromEmail;
    
    @Value("${app.name:AI Job Finder}")
    private String appName;
    
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendOTPEmail(String to, String otp, String purpose) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(purpose + " - OTP Verification");
        message.setText(buildOTPEmailBody(otp, purpose));
        mailSender.send(message);
    }
    
    private String buildOTPEmailBody(String otp, String purpose) {
        return String.format("""
            Hello,
            
            Your OTP for %s is: %s
            
            This OTP is valid for 10 minutes.
            
            If you didn't request this, please ignore this email.
            
            Best regards,
            %s Team
            """, purpose, otp, appName);
    }
    
    public void sendRegistrationOTP(String to, String otp) {
        sendOTPEmail(to, otp, "Email Verification");
    }
    
    public void sendForgotPasswordOTP(String to, String otp) {
        sendOTPEmail(to, otp, "Password Reset");
    }
}

