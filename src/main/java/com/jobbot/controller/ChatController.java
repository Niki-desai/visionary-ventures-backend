package com.jobbot.controller;

import com.jobbot.dto.*;
import com.jobbot.service.ChatService;
import com.jobbot.service.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "Chat history and messaging endpoints")
public class ChatController {
    
    private final ChatService chatService;
    private final JWTService jwtService;
    
    @Autowired
    public ChatController(ChatService chatService, JWTService jwtService) {
        this.chatService = chatService;
        this.jwtService = jwtService;
    }
    
    private String getUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        throw new RuntimeException("Invalid or missing token");
    }
    
    @PostMapping
    @Operation(
            summary = "Create Chat",
            description = "Create a new chat session for the authenticated user"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Chat created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiResponse> createChat(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateChatRequest request) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.createChat(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(
            summary = "Get User Chats",
            description = "Get all chats for the authenticated user with pagination"
    )
    public ResponseEntity<ApiResponse> getUserChats(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.getUserChats(userId, page, size, sortBy);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{chatId}")
    @Operation(
            summary = "Get Chat by ID",
            description = "Get a specific chat by ID (must belong to authenticated user)"
    )
    public ResponseEntity<ApiResponse> getChatById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String chatId) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.getChatById(userId, chatId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{chatId}")
    @Operation(
            summary = "Update Chat",
            description = "Update chat details (title, pin status, active status)"
    )
    public ResponseEntity<ApiResponse> updateChat(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String chatId,
            @Valid @RequestBody UpdateChatRequest request) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.updateChat(userId, chatId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{chatId}")
    @Operation(
            summary = "Delete Chat",
            description = "Delete a chat and all its messages"
    )
    public ResponseEntity<ApiResponse> deleteChat(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String chatId) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.deleteChat(userId, chatId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/messages")
    @Operation(
            summary = "Send Message",
            description = "Send a message in a chat"
    )
    public ResponseEntity<ApiResponse> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SendMessageRequest request) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.sendMessage(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{chatId}/messages")
    @Operation(
            summary = "Get Chat Messages",
            description = "Get all messages in a chat with pagination"
    )
    public ResponseEntity<ApiResponse> getChatMessages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String chatId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.getChatMessages(userId, chatId, page, size);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(
            summary = "Search Chats",
            description = "Search chats by title"
    )
    public ResponseEntity<ApiResponse> searchChats(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String query) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.searchChats(userId, query);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/pinned")
    @Operation(
            summary = "Get Pinned Chats",
            description = "Get all pinned chats for the authenticated user"
    )
    public ResponseEntity<ApiResponse> getPinnedChats(
            @RequestHeader("Authorization") String authHeader) {
        String userId = getUserIdFromToken(authHeader);
        ApiResponse response = chatService.getPinnedChats(userId);
        return ResponseEntity.ok(response);
    }
}

