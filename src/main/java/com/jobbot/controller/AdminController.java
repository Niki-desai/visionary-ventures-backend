package com.jobbot.controller;

import com.jobbot.service.DataSeederService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Administrative endpoints for database management")
public class AdminController {

    private final DataSeederService dataSeederService;

    @Autowired
    public AdminController(DataSeederService dataSeederService) {
        this.dataSeederService = dataSeederService;
    }

    @PostMapping("/seed")
    @Operation(
            summary = "Seed Database",
            description = "Populates the database with sample data including users, jobs, resumes, applications, and AI conversations. " +
                    "⚠️ Warning: This will only seed if database is empty."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database seeded successfully"),
            @ApiResponse(responseCode = "500", description = "Failed to seed database")
    })
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

