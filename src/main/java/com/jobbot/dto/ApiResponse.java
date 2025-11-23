package com.jobbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic API response")
public class ApiResponse {
    
    @Schema(description = "Response status", example = "success")
    private String status;
    
    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;
    
    @Schema(description = "Response data")
    private Object data;
    
    public ApiResponse() {}
    
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public static ApiResponse success(String message) {
        return new ApiResponse("success", message);
    }
    
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse("success", message, data);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse("error", message);
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}

