# MongoDB Complete Guide - Setup, Schema & Models

## 📚 Table of Contents

1. [MongoDB Setup](#mongodb-setup)
2. [Schema Overview](#schema-overview)
3. [Detailed Schema](#detailed-schema)
4. [Models Summary](#models-summary)
5. [Relationships](#relationships)
6. [Indexes](#indexes)

---

## MongoDB Setup

### Issue: Collections/Tables Not Showing

If you're only seeing startup logs but no collections in MongoDB, follow these steps:

---

## Step 1: Install MongoDB

### Option A: MongoDB Community Server (Local)

1. **Download MongoDB:**
   - Visit: https://www.mongodb.com/try/download/community
   - Select: Windows, MSI package
   - Download and install

2. **Add to PATH (if not auto-added):**
   - Add `C:\Program Files\MongoDB\Server\<version>\bin` to your PATH
   - Or use full path: `C:\Program Files\MongoDB\Server\<version>\bin\mongod.exe`

3. **Start MongoDB:**
   ```powershell
   # Create data directory
   mkdir C:\data\db
   
   # Start MongoDB
   mongod --dbpath C:\data\db
   ```

### Option B: MongoDB Atlas (Cloud - Recommended)

1. **Sign up:** https://www.mongodb.com/cloud/atlas/register
2. **Create free cluster**
3. **Get connection string**
4. **Update application.yml:**
   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb+srv://username:password@cluster.mongodb.net/jobbot
   ```

---

## Step 2: Verify MongoDB is Running

### Check if MongoDB is running:

```powershell
# Check if MongoDB process is running
Get-Process mongod -ErrorAction SilentlyContinue

# Or check port 27017
netstat -ano | findstr :27017
```

### Test Connection:

```powershell
# If mongosh is installed
mongosh

# Or use MongoDB Compass (GUI)
# Download: https://www.mongodb.com/try/download/compass
```

---

## Step 3: Run Application

1. **Start MongoDB first:**
   ```powershell
   mongod --dbpath C:\data\db
   ```

2. **In another terminal, run application:**
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
   $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"
   .\mvnw.cmd spring-boot:run
   ```

3. **Look for these logs:**
   ```
   🔌 Connected to MongoDB database: jobbot
   📊 Creating MongoDB indexes...
   ✅ User indexes created
   ✅ Job indexes created
   ...
   🌱 Seeding MongoDB database...
   ✅ Created sample user: demo@jobbot.com
   ✅ Created 2 sample jobs
   ...
   🎉 Database seeding completed!
   ```

---

## Step 4: Verify Collections Created

### Using MongoDB Compass:
1. Connect to: `mongodb://localhost:27017`
2. Select database: `jobbot`
3. You should see collections:
   - `users`
   - `jobs`
   - `applications`
   - `resumes`
   - `job_searches`
   - `ai_conversations`
   - `chats` (NEW)
   - `chat_messages` (NEW)

### Using MongoDB Shell:
```javascript
mongosh
use jobbot
show collections
db.users.find()
db.jobs.find()
```

---

## Step 5: Manual Seeding (If Auto-Seed Didn't Work)

If collections are created but empty, manually seed:

```powershell
# Using curl
curl -X POST http://localhost:8080/api/admin/seed

# Or using PowerShell
Invoke-WebRequest -Uri http://localhost:8080/api/admin/seed -Method POST
```

---

## Troubleshooting

### Error: "MongoDB connection failed"

**Solution:**
1. Make sure MongoDB is running
2. Check connection string in `application.yml`
3. Verify port 27017 is not blocked by firewall

### Error: "Collections not created"

**Solution:**
- Collections are created automatically when first document is inserted
- Run the seed endpoint: `POST /api/admin/seed`

### Error: "MongoDB not found"

**Solution:**
- Install MongoDB or use MongoDB Atlas (cloud)
- Update connection string in `application.yml`

---

## Quick Test

After setup, test the connection:

```powershell
# Check if application can connect
curl http://localhost:8080/api/health

# Seed data
curl -X POST http://localhost:8080/api/admin/seed

# Check MongoDB
mongosh
use jobbot
db.users.countDocuments()
```

---

## MongoDB Atlas Setup (Easiest)

1. Go to: https://www.mongodb.com/cloud/atlas
2. Create free account
3. Create free cluster (M0)
4. Get connection string
5. Update `application.yml`:
   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/jobbot?retryWrites=true&w=majority
   ```

No local installation needed! 🎉

---

## Schema Overview

### Database: `jobbot`

**Total Collections: 8**

1. **users** - User accounts and profiles
2. **jobs** - Job postings
3. **applications** - Job applications
4. **resumes** - User resumes
5. **job_searches** - Saved job searches
6. **ai_conversations** - AI conversations
7. **chats** - Chat sessions (NEW)
8. **chat_messages** - Chat messages (NEW)

---

## Models Summary

### 1. **User Model** (`User.java`)
- **Collection**: `users`
- **Purpose**: User accounts, profiles, and preferences
- **Key Features**: Email (unique), subscription tiers, job preferences, auto-apply settings
- **Repository**: `UserRepository.java`

### 2. **Job Model** (`Job.java`)
- **Collection**: `jobs`
- **Purpose**: Job postings from various sources
- **Key Features**: Location, salary, skills, AI analysis, multiple sources
- **Repository**: `JobRepository.java`

### 3. **Application Model** (`Application.java`)
- **Collection**: `applications`
- **Purpose**: Job applications submitted by users
- **Key Features**: Status tracking, AI insights, cover letter generation, tracking info
- **Repository**: `ApplicationRepository.java`

### 4. **Resume Model** (`Resume.java`)
- **Collection**: `resumes`
- **Purpose**: User resumes/CVs
- **Key Features**: Experience, education, skills, certifications, projects, file storage
- **Repository**: `ResumeRepository.java`

### 5. **JobSearch Model** (`JobSearch.java`)
- **Collection**: `job_searches`
- **Purpose**: Saved job search criteria and alerts
- **Key Features**: Search criteria, alert settings, notification preferences
- **Repository**: `JobSearchRepository.java`

### 6. **AIConversation Model** (`AIConversation.java`)
- **Collection**: `ai_conversations`
- **Purpose**: AI agent conversations and interactions
- **Key Features**: Message history, context, conversation types, token tracking
- **Repository**: `AIConversationRepository.java`

### 7. **Chat Model** (`Chat.java`)
- **Collection**: `chats`
- **Purpose**: Chat sessions for users (multiple chats per user)
- **Key Features**: Title, chat type, pinned status, message count
- **Repository**: `ChatRepository.java`

### 8. **ChatMessage Model** (`ChatMessage.java`)
- **Collection**: `chat_messages`
- **Purpose**: Individual messages within chat sessions
- **Key Features**: Role, content, message type, tokens used, threading
- **Repository**: `ChatMessageRepository.java`

---

## File Structure

```
src/main/java/com/jobbot/
├── model/
│   ├── User.java
│   ├── Job.java
│   ├── Application.java
│   ├── Resume.java
│   ├── JobSearch.java
│   ├── AIConversation.java
│   ├── Chat.java
│   └── ChatMessage.java
├── repository/
│   ├── UserRepository.java
│   ├── JobRepository.java
│   ├── ApplicationRepository.java
│   ├── ResumeRepository.java
│   ├── JobSearchRepository.java
│   ├── AIConversationRepository.java
│   ├── ChatRepository.java
│   └── ChatMessageRepository.java
└── ...
```

---

## Detailed Schema

### 1. **users** Collection

**Indexes:**
- `email` (unique)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `email`: String (unique, indexed)
- `passwordHash`: String
- `firstName`, `lastName`: String
- `preferences`: Embedded document (job alerts, notifications, auto-apply settings)
- `subscriptionTier`: Enum (FREE, BASIC, PREMIUM, ENTERPRISE)
- `isActive`: Boolean
- `emailVerified`: Boolean
- `createdAt`, `updatedAt`, `lastLogin`: DateTime

**Relationships:**
- One-to-Many: User → Applications
- One-to-Many: User → Resumes
- One-to-Many: User → JobSearches
- One-to-Many: User → AIConversations
- One-to-Many: User → Chats

---

### 2. **jobs** Collection

**Indexes:**
- `title` (text search)
- `companyId`
- `jobType`
- `industry`
- `remoteType`

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `title`: String (indexed)
- `description`: String
- `companyName`, `companyId`: String (indexed)
- `location`: Embedded document (city, state, country, coordinates)
- `salary`: Embedded document (min, max, currency, type)
- `requiredSkills`: Array of Strings
- `isActive`: Boolean
- `expiryDate`: DateTime
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- One-to-Many: Job → Applications

---

### 3. **applications** Collection

**Indexes:**
- `userId + jobId` (compound)
- `userId + status.current` (compound)
- `jobId`
- `submittedAt` (descending)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `jobId`: String (indexed, references jobs)
- `resumeId`: String (references resumes)
- `status`: Embedded document (current, history with timestamps)
- `coverLetter`: String
- `aiInsights`: Embedded document (strengths, weaknesses, suggestions)
- `tracking`: Embedded document (email opens, profile views)
- `submittedAt`: DateTime
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- Many-to-One: Application → User
- Many-to-One: Application → Job
- Many-to-One: Application → Resume

---

### 4. **resumes** Collection

**Indexes:**
- `userId + isDefault` (compound)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `isDefault`: Boolean
- `summary`: String
- `experience`: Array of embedded documents (company, position, dates, achievements)
- `education`: Array of embedded documents (institution, degree, dates, GPA)
- `skills`: Array of Strings
- `certifications`: Array of embedded documents
- `languages`: Array of embedded documents
- `projects`: Array of embedded documents
- `fileUrl`: String (PDF/DOCX storage URL)
- `fileFormat`: String - "pdf", "docx", "json"
- `aiEnhanced`: Boolean
- `version`: Integer
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- Many-to-One: Resume → User
- One-to-Many: Resume → Applications

---

### 5. **job_searches** Collection

**Indexes:**
- `userId`
- `userId + isActive` (compound)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `name`: String (user-friendly name)
- `isActive`: Boolean
- `searchCriteria`: Embedded document (keywords, locations, jobTypes, salary range, skills)
- `alertSettings`: Embedded document (enabled, frequency, notifications)
- `lastSearchedAt`: DateTime
- `resultsCount`: Integer
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- Many-to-One: JobSearch → User

---

### 6. **ai_conversations** Collection

**Indexes:**
- `userId`
- `userId + conversationType` (compound)
- `userId + isActive` (compound)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `conversationType`: String - "job_search", "resume_review", "cover_letter", "interview_prep", "general"
- `context`: Embedded document (jobId, applicationId, resumeId, searchId, userPreferences)
- `messages`: Array of embedded documents (role, content, timestamp, tokensUsed, modelUsed)
- `summary`: String
- `isActive`: Boolean
- `metadata`: Map<String, Object>
- `createdAt`, `updatedAt`: DateTime

**Message Roles:**
- "user", "assistant", "system"

**Relationships:**
- Many-to-One: AIConversation → User
- Optional: AIConversation → Job, Application, Resume, JobSearch

---

### 7. **chats** Collection

**Indexes:**
- `user_id`
- `user_id + is_active` (compound)
- `user_id + is_pinned` (compound)
- `user_id + last_message_at` (descending)
- `title` (text search)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `title`: String (user-friendly chat title)
- `chatType`: String - "general", "job_search", "resume_review", "cover_letter", "interview_prep"
- `isActive`: Boolean
- `isPinned`: Boolean (user can pin important chats)
- `lastMessageAt`: DateTime (for sorting by recent activity)
- `messageCount`: Integer (total messages in chat)
- `metadata`: Map<String, Object> (additional context like jobId, applicationId)
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- Many-to-One: Chat → User
- One-to-Many: Chat → ChatMessage

---

### 8. **chat_messages** Collection

**Indexes:**
- `chat_id`
- `user_id`
- `chat_id + created_at` (descending)
- `parent_message_id` (for threaded conversations)

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `chatId`: String (indexed, references chats)
- `userId`: String (indexed, for quick user queries)
- `role`: String - "user", "assistant", "system"
- `content`: String (message text)
- `messageType`: String - "text", "image", "file", "code", "markdown"
- `tokensUsed`: Integer (for AI responses)
- `modelUsed`: String (AI model name, e.g., "gpt-4", "claude-3")
- `isEdited`: Boolean
- `editedAt`: DateTime
- `parentMessageId`: String (for threaded conversations)
- `metadata`: Map<String, Object> (attachments, formatting, etc.)
- `createdAt`: DateTime

**Relationships:**
- Many-to-One: ChatMessage → Chat
- Many-to-One: ChatMessage → User
- Optional: ChatMessage → ChatMessage (parent message for threading)

---

## Relationships

```
User (1) ──< (Many) Application
User (1) ──< (Many) Resume
User (1) ──< (Many) JobSearch
User (1) ──< (Many) AIConversation
User (1) ──< (Many) Chat

Job (1) ──< (Many) Application
Resume (1) ──< (Many) Application

Chat (1) ──< (Many) ChatMessage

AIConversation ──> (Optional) Job, Application, Resume, JobSearch
```

---

## Indexes

### Recommended Indexes (Auto-created by MongoIndexConfig):

```javascript
// users collection
db.users.createIndex({ "email": 1 }, { unique: true })

// jobs collection
db.jobs.createIndex({ "title": "text", "description": "text" })
db.jobs.createIndex({ "companyId": 1 })
db.jobs.createIndex({ "jobType": 1 })
db.jobs.createIndex({ "industry": 1 })
db.jobs.createIndex({ "remoteType": 1 })
db.jobs.createIndex({ "isActive": 1, "expiryDate": 1 })

// applications collection
db.applications.createIndex({ "userId": 1, "jobId": 1 })
db.applications.createIndex({ "userId": 1, "status.current": 1 })
db.applications.createIndex({ "jobId": 1 })
db.applications.createIndex({ "submittedAt": -1 })

// resumes collection
db.resumes.createIndex({ "userId": 1, "isDefault": 1 })

// job_searches collection
db.job_searches.createIndex({ "userId": 1, "isActive": 1 })

// ai_conversations collection
db.ai_conversations.createIndex({ "userId": 1, "conversationType": 1 })
db.ai_conversations.createIndex({ "userId": 1, "isActive": 1 })

// chats collection
db.chats.createIndex({ "user_id": 1 })
db.chats.createIndex({ "user_id": 1, "is_active": 1 })
db.chats.createIndex({ "user_id": 1, "is_pinned": 1 })
db.chats.createIndex({ "user_id": 1, "last_message_at": -1 })
db.chats.createIndex({ "title": "text" })

// chat_messages collection
db.chat_messages.createIndex({ "chat_id": 1 })
db.chat_messages.createIndex({ "user_id": 1 })
db.chat_messages.createIndex({ "chat_id": 1, "created_at": -1 })
db.chat_messages.createIndex({ "parent_message_id": 1 })
```

---

## Key Features

### AI Integration
- **Job Analysis**: AI match scores, skill matching, recommendations
- **Application Insights**: Strengths, weaknesses, suggestions
- **Cover Letter Generation**: AI-powered cover letter creation
- **Conversations**: Full AI conversation history with context

### Application Tracking
- Status history with timestamps
- Email tracking (opens, views)
- Profile view tracking
- Follow-up reminders

### Job Search
- Advanced search criteria
- Saved searches with alerts
- Multiple notification channels
- Real-time job matching

### Resume Management
- Multiple resume versions
- Default resume selection
- AI-enhanced resumes
- File storage support (PDF/DOCX)

### Chat System
- Multiple chats per user
- Message history
- Threading support
- AI integration ready

---

## All Models Include

- ✅ MongoDB `@Document` annotations
- ✅ Proper field mappings with `@Field`
- ✅ Indexed fields with `@Indexed`
- ✅ Embedded documents for nested structures
- ✅ Timestamps (createdAt, updatedAt)
- ✅ Complete getters and setters
- ✅ Repository interfaces with custom queries

---

**Status**: ✅ Complete - All models, repositories, and indexes created successfully!

