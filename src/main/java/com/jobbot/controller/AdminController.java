package com.jobbot.controller;

import com.jobbot.service.DataSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DataSeederService dataSeederService;

    @Autowired
    public AdminController(DataSeederService dataSeederService) {
        this.dataSeederService = dataSeederService;
    }

    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> seedData() {
        try {
            dataSeederService.seedData();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Database seeded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to seed database: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

