package com.jobbot.controller;

import com.jobbot.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health check and system status endpoints")
public class HealthController {

    private final HealthService healthService;

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    @Operation(
            summary = "Health Check",
            description = "Returns the health status of the application including service status, timestamp, and version information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service is healthy and running"),
            @ApiResponse(responseCode = "503", description = "Service is unavailable")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(healthService.getHealthStatus());
    }
}

