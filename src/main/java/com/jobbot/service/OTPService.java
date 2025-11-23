package com.jobbot.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OTPService {
    
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;
    private final SecureRandom random = new SecureRandom();
    
    // In-memory OTP storage (in production, use Redis or database)
    private final ConcurrentMap<String, OTPData> otpStore = new ConcurrentHashMap<>();
    
    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    
    public void storeOTP(String email, String otp, OTPType type) {
        OTPData otpData = new OTPData(otp, LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES), type);
        otpStore.put(email, otpData);
    }
    
    public boolean verifyOTP(String email, String otp, OTPType type) {
        OTPData otpData = otpStore.get(email);
        if (otpData == null) {
            return false;
        }
        
        if (otpData.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            return false;
        }
        
        if (!otpData.getOtp().equals(otp) || otpData.getType() != type) {
            return false;
        }
        
        // OTP verified successfully, remove it
        otpStore.remove(email);
        return true;
    }
    
    public void removeOTP(String email) {
        otpStore.remove(email);
    }
    
    public boolean hasOTP(String email) {
        OTPData otpData = otpStore.get(email);
        if (otpData == null) {
            return false;
        }
        if (otpData.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(email);
            return false;
        }
        return true;
    }
    
    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;
        private final OTPType type;
        
        public OTPData(String otp, LocalDateTime expiryTime, OTPType type) {
            this.otp = otp;
            this.expiryTime = expiryTime;
            this.type = type;
        }
        
        public String getOtp() {
            return otp;
        }
        
        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
        
        public OTPType getType() {
            return type;
        }
    }
    
    public enum OTPType {
        REGISTRATION,
        FORGOT_PASSWORD
    }
}

