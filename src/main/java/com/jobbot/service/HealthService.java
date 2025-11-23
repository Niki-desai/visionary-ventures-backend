package com.jobbot.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    public Map<String, Object> getHealthStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        status.put("service", "Visionary Ventures Backend");
        status.put("version", "1.0.0");
        return status;
    }
}

