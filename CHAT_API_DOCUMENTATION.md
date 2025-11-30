# Chat History API Documentation

## 📚 Overview

Complete chat history system with support for:
- Multiple chats per user
- Message history
- Chat management (create, update, delete)
- Search functionality
- Pinned chats

---

## 🔐 Authentication

All endpoints require JWT authentication:
```
Authorization: Bearer <token>
```

---

## 📋 API Endpoints

### 1. **Create Chat**

**POST** `/api/chats`

Create a new chat session.

**Request Body:**
```json
{
  "title": "Job Search Help",
  "chatType": "job_search",
  "metadata": {
    "jobId": "507f1f77bcf86cd799439011"
  }
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Chat created successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "userId": "507f1f77bcf86cd799439012",
    "title": "Job Search Help",
    "chatType": "job_search",
    "isActive": true,
    "isPinned": false,
    "lastMessageAt": null,
    "messageCount": 0,
    "metadata": {
      "jobId": "507f1f77bcf86cd799439011"
    },
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

**Chat Types:**
- `general` (default)
- `job_search`
- `resume_review`
- `cover_letter`
- `interview_prep`

---

### 2. **Get User Chats**

**GET** `/api/chats?page=0&size=20&sortBy=lastMessageAt`

Get all chats for authenticated user with pagination.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sortBy` (optional): Sort field (default: "lastMessageAt")

**Response:**
```json
{
  "status": "success",
  "message": "Chats retrieved successfully",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "title": "Job Search Help",
      "chatType": "job_search",
      "isActive": true,
      "isPinned": false,
      "lastMessageAt": "2024-01-15T11:00:00",
      "messageCount": 5,
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

---

### 3. **Get Chat by ID**

**GET** `/api/chats/{chatId}`

Get a specific chat (must belong to authenticated user).

**Response:**
```json
{
  "status": "success",
  "message": "Chat retrieved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "title": "Job Search Help",
    "chatType": "job_search",
    "isActive": true,
    "isPinned": false,
    "lastMessageAt": "2024-01-15T11:00:00",
    "messageCount": 5,
    "metadata": {},
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
}
```

---

### 4. **Update Chat**

**PUT** `/api/chats/{chatId}`

Update chat details (title, pin status, active status).

**Request Body:**
```json
{
  "title": "Updated Chat Title",
  "isPinned": true,
  "isActive": true
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Chat updated successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "title": "Updated Chat Title",
    "isPinned": true,
    "isActive": true,
    "updatedAt": "2024-01-15T11:30:00"
  }
}
```

---

### 5. **Delete Chat**

**DELETE** `/api/chats/{chatId}`

Delete a chat and all its messages.

**Response:**
```json
{
  "status": "success",
  "message": "Chat deleted successfully"
}
```

---

### 6. **Send Message**

**POST** `/api/chats/messages`

Send a message in a chat.

**Request Body:**
```json
{
  "chatId": "507f1f77bcf86cd799439011",
  "content": "Can you help me find a job?",
  "messageType": "text",
  "parentMessageId": null,
  "metadata": {}
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Message sent successfully",
  "data": {
    "id": "507f1f77bcf86cd799439012",
    "chatId": "507f1f77bcf86cd799439011",
    "userId": "507f1f77bcf86cd799439013",
    "role": "user",
    "content": "Can you help me find a job?",
    "messageType": "text",
    "isEdited": false,
    "createdAt": "2024-01-15T11:00:00"
  }
}
```

**Message Types:**
- `text` (default)
- `image`
- `file`
- `code`
- `markdown`

---

### 7. **Get Chat Messages**

**GET** `/api/chats/{chatId}/messages?page=0&size=50`

Get all messages in a chat with pagination.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 50)

**Response:**
```json
{
  "status": "success",
  "message": "Messages retrieved successfully",
  "data": [
    {
      "id": "507f1f77bcf86cd799439012",
      "chatId": "507f1f77bcf86cd799439011",
      "role": "user",
      "content": "Can you help me find a job?",
      "messageType": "text",
      "createdAt": "2024-01-15T11:00:00"
    },
    {
      "id": "507f1f77bcf86cd799439013",
      "chatId": "507f1f77bcf86cd799439011",
      "role": "assistant",
      "content": "I'd be happy to help you find a job!",
      "messageType": "text",
      "tokensUsed": 15,
      "modelUsed": "gpt-4",
      "createdAt": "2024-01-15T11:00:05"
    }
  ]
}
```

---

### 8. **Search Chats**

**GET** `/api/chats/search?query=job`

Search chats by title.

**Query Parameters:**
- `query` (required): Search term

**Response:**
```json
{
  "status": "success",
  "message": "Chats found",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "title": "Job Search Help",
      "chatType": "job_search",
      "messageCount": 5
    }
  ]
}
```

---

### 9. **Get Pinned Chats**

**GET** `/api/chats/pinned`

Get all pinned chats for authenticated user.

**Response:**
```json
{
  "status": "success",
  "message": "Pinned chats retrieved",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "title": "Important Chat",
      "isPinned": true,
      "messageCount": 10
    }
  ]
}
```

---

## 🔄 Complete Flow Example

### 1. Create Chat
```bash
POST /api/chats
Authorization: Bearer <token>
{
  "title": "My First Chat",
  "chatType": "general"
}
```

### 2. Send Message
```bash
POST /api/chats/messages
Authorization: Bearer <token>
{
  "chatId": "<chat-id-from-step-1>",
  "content": "Hello, I need help!"
}
```

### 3. Get Messages
```bash
GET /api/chats/<chat-id>/messages
Authorization: Bearer <token>
```

### 4. Update Chat (Pin it)
```bash
PUT /api/chats/<chat-id>
Authorization: Bearer <token>
{
  "isPinned": true
}
```

---

## 📊 Database Schema

### Chat Collection
- `id`: Chat ID
- `userId`: User ID (indexed)
- `title`: Chat title
- `chatType`: Type of chat
- `isActive`: Active status
- `isPinned`: Pinned status
- `lastMessageAt`: Last message timestamp
- `messageCount`: Total messages
- `metadata`: Additional context
- `createdAt`, `updatedAt`: Timestamps

### ChatMessage Collection
- `id`: Message ID
- `chatId`: Chat ID (indexed)
- `userId`: User ID (indexed)
- `role`: "user", "assistant", "system"
- `content`: Message content
- `messageType`: Type of message
- `tokensUsed`: Tokens used (for AI)
- `modelUsed`: AI model name
- `isEdited`: Edit status
- `parentMessageId`: Parent message (for threading)
- `metadata`: Additional data
- `createdAt`: Timestamp

---

## 🎯 Use Cases

1. **Multiple Chat Sessions**
   - User can have multiple ongoing chats
   - Each chat has its own history
   - Chats can be organized by type

2. **Message History**
   - All messages stored with timestamps
   - Pagination for large conversations
   - Thread support (parent messages)

3. **Chat Management**
   - Pin important chats
   - Archive/deactivate chats
   - Search by title
   - Update chat details

4. **AI Integration**
   - Track tokens used
   - Store model information
   - Support for different message types

---

## 🚀 Testing with Swagger

1. Start the application
2. Open: `http://localhost:8080/swagger-ui.html`
3. Find "Chats" section
4. Try all endpoints interactively!

---

## 💡 Tips

- Use pagination for large message lists
- Pin frequently used chats
- Use metadata to store context (jobId, applicationId, etc.)
- Support threaded conversations with `parentMessageId`
- Track AI usage with `tokensUsed` and `modelUsed`

---

**Happy Chatting! 💬**

