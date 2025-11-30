package com.jobbot.service;

import com.jobbot.dto.*;
import com.jobbot.model.Chat;
import com.jobbot.model.ChatMessage;
import com.jobbot.repository.ChatMessageRepository;
import com.jobbot.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    public ApiResponse createChat(String userId, CreateChatRequest request) {
        Chat chat = new Chat();
        chat.setUserId(userId);
        chat.setTitle(request.getTitle());
        chat.setChatType(request.getChatType() != null ? request.getChatType() : "general");
        chat.setIsActive(true);
        chat.setIsPinned(false);
        chat.setMessageCount(0);
        chat.setMetadata(request.getMetadata());
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());
        
        Chat savedChat = chatRepository.save(chat);
        
        ChatResponse response = convertToChatResponse(savedChat);
        return ApiResponse.success("Chat created successfully", response);
    }
    
    public ApiResponse getUserChats(String userId, Integer page, Integer size, String sortBy) {
        Pageable pageable = PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 20,
            Sort.by(Sort.Direction.DESC, sortBy != null ? sortBy : "lastMessageAt")
        );
        
        Page<Chat> chatsPage = chatRepository.findByUserIdOrderByLastMessageAtDesc(userId, pageable);
        
        List<ChatResponse> chatResponses = chatsPage.getContent().stream()
            .map(this::convertToChatResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success("Chats retrieved successfully", chatResponses);
    }
    
    public ApiResponse getChatById(String userId, String chatId) {
        Optional<Chat> chatOpt = chatRepository.findByIdAndUserId(chatId, userId);
        
        if (chatOpt.isEmpty()) {
            return ApiResponse.error("Chat not found");
        }
        
        ChatResponse response = convertToChatResponse(chatOpt.get());
        return ApiResponse.success("Chat retrieved successfully", response);
    }
    
    public ApiResponse updateChat(String userId, String chatId, UpdateChatRequest request) {
        Optional<Chat> chatOpt = chatRepository.findByIdAndUserId(chatId, userId);
        
        if (chatOpt.isEmpty()) {
            return ApiResponse.error("Chat not found");
        }
        
        Chat chat = chatOpt.get();
        
        if (request.getTitle() != null) {
            chat.setTitle(request.getTitle());
        }
        if (request.getIsPinned() != null) {
            chat.setIsPinned(request.getIsPinned());
        }
        if (request.getIsActive() != null) {
            chat.setIsActive(request.getIsActive());
        }
        
        chat.setUpdatedAt(LocalDateTime.now());
        Chat updatedChat = chatRepository.save(chat);
        
        ChatResponse response = convertToChatResponse(updatedChat);
        return ApiResponse.success("Chat updated successfully", response);
    }
    
    public ApiResponse deleteChat(String userId, String chatId) {
        Optional<Chat> chatOpt = chatRepository.findByIdAndUserId(chatId, userId);
        
        if (chatOpt.isEmpty()) {
            return ApiResponse.error("Chat not found");
        }
        
        // Delete all messages in the chat
        chatMessageRepository.deleteByChatId(chatId);
        
        // Delete the chat
        chatRepository.deleteById(chatId);
        
        return ApiResponse.success("Chat deleted successfully");
    }
    
    public ApiResponse sendMessage(String userId, SendMessageRequest request) {
        // Verify chat belongs to user
        Optional<Chat> chatOpt = chatRepository.findByIdAndUserId(request.getChatId(), userId);
        
        if (chatOpt.isEmpty()) {
            return ApiResponse.error("Chat not found");
        }
        
        Chat chat = chatOpt.get();
        
        if (!chat.getIsActive()) {
            return ApiResponse.error("Chat is not active");
        }
        
        // Create message
        ChatMessage message = new ChatMessage();
        message.setChatId(request.getChatId());
        message.setUserId(userId);
        message.setRole("user");
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType() != null ? request.getMessageType() : "text");
        message.setParentMessageId(request.getParentMessageId());
        message.setMetadata(request.getMetadata());
        message.setIsEdited(false);
        message.setCreatedAt(LocalDateTime.now());
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        
        // Update chat
        chat.setLastMessageAt(LocalDateTime.now());
        chat.setMessageCount(chat.getMessageCount() + 1);
        chat.setUpdatedAt(LocalDateTime.now());
        chatRepository.save(chat);
        
        ChatMessageResponse response = convertToMessageResponse(savedMessage);
        return ApiResponse.success("Message sent successfully", response);
    }
    
    public ApiResponse getChatMessages(String userId, String chatId, Integer page, Integer size) {
        // Verify chat belongs to user
        Optional<Chat> chatOpt = chatRepository.findByIdAndUserId(chatId, userId);
        
        if (chatOpt.isEmpty()) {
            return ApiResponse.error("Chat not found");
        }
        
        Pageable pageable = PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 50,
            Sort.by(Sort.Direction.ASC, "createdAt")
        );
        
        Page<ChatMessage> messagesPage = chatMessageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);
        
        List<ChatMessageResponse> messageResponses = messagesPage.getContent().stream()
            .map(this::convertToMessageResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success("Messages retrieved successfully", messageResponses);
    }
    
    public ApiResponse searchChats(String userId, String query) {
        List<Chat> chats = chatRepository.findByUserIdAndTitleContaining(userId, query);
        
        List<ChatResponse> chatResponses = chats.stream()
            .map(this::convertToChatResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success("Chats found", chatResponses);
    }
    
    public ApiResponse getPinnedChats(String userId) {
        List<Chat> pinnedChats = chatRepository.findByUserIdAndIsPinnedTrue(userId);
        
        List<ChatResponse> chatResponses = pinnedChats.stream()
            .map(this::convertToChatResponse)
            .collect(Collectors.toList());
        
        return ApiResponse.success("Pinned chats retrieved", chatResponses);
    }
    
    // Helper methods
    private ChatResponse convertToChatResponse(Chat chat) {
        ChatResponse response = new ChatResponse();
        response.setId(chat.getId());
        response.setUserId(chat.getUserId());
        response.setTitle(chat.getTitle());
        response.setChatType(chat.getChatType());
        response.setIsActive(chat.getIsActive());
        response.setIsPinned(chat.getIsPinned());
        response.setLastMessageAt(chat.getLastMessageAt());
        response.setMessageCount(chat.getMessageCount());
        response.setMetadata(chat.getMetadata());
        response.setCreatedAt(chat.getCreatedAt());
        response.setUpdatedAt(chat.getUpdatedAt());
        return response;
    }
    
    private ChatMessageResponse convertToMessageResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setChatId(message.getChatId());
        response.setUserId(message.getUserId());
        response.setRole(message.getRole());
        response.setContent(message.getContent());
        response.setMessageType(message.getMessageType());
        response.setTokensUsed(message.getTokensUsed());
        response.setModelUsed(message.getModelUsed());
        response.setIsEdited(message.getIsEdited());
        response.setEditedAt(message.getEditedAt());
        response.setParentMessageId(message.getParentMessageId());
        response.setMetadata(message.getMetadata());
        response.setCreatedAt(message.getCreatedAt());
        return response;
    }
}

