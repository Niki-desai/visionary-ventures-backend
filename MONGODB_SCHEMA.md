# MongoDB Schema Documentation
## AI Job Finder & Auto-Application Assistant

### Database: `jobbot`

---

## Collections Overview

### 1. **users** Collection
Stores user accounts and profiles.

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

---

### 2. **jobs** Collection
Stores job postings from various sources.

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
- `jobType`: String (indexed) - "full-time", "part-time", "contract", "internship"
- `remoteType`: String (indexed) - "remote", "hybrid", "onsite"
- `salary`: Embedded document (min, max, currency, period)
- `industry`: String (indexed)
- `requiredSkills`, `preferredSkills`: Array of Strings
- `experienceLevel`: String - "entry", "mid", "senior", "executive"
- `source`: Embedded document (name, apiProvider, scraped)
- `externalId`, `externalUrl`: String
- `applicationUrl`: String
- `postedDate`, `expiryDate`: DateTime
- `isActive`: Boolean
- `applicationCount`: Integer
- `aiAnalysis`: Embedded document (matchScore, skillMatchPercentage, recommended, reasoning)
- `createdAt`, `updatedAt`: DateTime

**Relationships:**
- One-to-Many: Job → Applications

**Sample Document:**
```json
{
  "_id": "507f1f77bcf86cd799439011",
  "title": "Senior Software Engineer",
  "companyName": "Tech Corp",
  "location": {
    "city": "San Francisco",
    "state": "CA",
    "country": "USA"
  },
  "jobType": "full-time",
  "remoteType": "hybrid",
  "salary": {
    "min": 120000,
    "max": 180000,
    "currency": "USD",
    "period": "yearly"
  },
  "requiredSkills": ["Java", "Spring Boot", "MongoDB"],
  "aiAnalysis": {
    "matchScore": 0.85,
    "recommended": true
  }
}
```

---

### 3. **applications** Collection
Stores job applications submitted by users.

**Indexes:**
- `userId` (compound with jobId)
- `jobId`
- `userId + status.current`

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `jobId`: String (indexed, references jobs)
- `resumeId`: String (references resumes)
- `status`: Embedded document (current, statusHistory, lastUpdated)
- `coverLetter`: String
- `coverLetterGenerated`: Boolean (AI generated or user written)
- `applicationMethod`: String - "auto", "manual", "ai_assisted"
- `submittedAt`: DateTime
- `externalApplicationId`: String
- `trackingInfo`: Embedded document (email tracking, profile views)
- `aiInsights`: Embedded document (matchScore, strengths, weaknesses, suggestions)
- `notes`: String
- `followUpDate`: DateTime
- `createdAt`, `updatedAt`: DateTime

**Status Values:**
- "draft", "submitted", "under_review", "interview", "offer", "rejected", "withdrawn"

**Relationships:**
- Many-to-One: Application → User
- Many-to-One: Application → Job
- Many-to-One: Application → Resume

---

### 4. **resumes** Collection
Stores user resumes/CVs.

**Indexes:**
- `userId`

**Key Fields:**
- `id`: ObjectId (Primary Key)
- `userId`: String (indexed, references users)
- `title`: String
- `isDefault`: Boolean
- `personalInfo`: Embedded document (name, email, phone, links)
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
Stores saved job search criteria and alerts.

**Indexes:**
- `userId`

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
Stores AI agent conversations and interactions.

**Indexes:**
- `userId`
- `userId + conversationType`

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

## Indexes Summary

### Recommended Indexes (to be created):

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
db.jobs.createIndex({ "requiredSkills": 1 })

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
```

---

## Data Relationships Diagram

```
User (1) ──< (Many) Application
User (1) ──< (Many) Resume
User (1) ──< (Many) JobSearch
User (1) ──< (Many) AIConversation

Job (1) ──< (Many) Application
Resume (1) ──< (Many) Application

AIConversation (0..1) ──> (0..1) Job
AIConversation (0..1) ──> (0..1) Application
AIConversation (0..1) ──> (0..1) Resume
AIConversation (0..1) ──> (0..1) JobSearch
```

---

## Spring Boot Model Classes

All models are located in: `src/main/java/com/jobbot/model/`

1. **User.java** - `@Document(collection = "users")`
2. **Job.java** - `@Document(collection = "jobs")`
3. **Application.java** - `@Document(collection = "applications")`
4. **Resume.java** - `@Document(collection = "resumes")`
5. **JobSearch.java** - `@Document(collection = "job_searches")`
6. **AIConversation.java** - `@Document(collection = "ai_conversations")`

---

## Repository Interfaces

All repositories are located in: `src/main/java/com/jobbot/repository/`

1. **UserRepository** - extends `MongoRepository<User, String>`
2. **JobRepository** - extends `MongoRepository<Job, String>`
3. **ApplicationRepository** - extends `MongoRepository<Application, String>`
4. **ResumeRepository** - extends `MongoRepository<Resume, String>`
5. **JobSearchRepository** - extends `MongoRepository<JobSearch, String>`
6. **AIConversationRepository** - extends `MongoRepository<AIConversation, String>`

---

## Usage Examples

### Find User by Email
```java
Optional<User> user = userRepository.findByEmail("user@example.com");
```

### Find Active Jobs
```java
List<Job> activeJobs = jobRepository.findByIsActiveTrue();
```

### Find User Applications
```java
List<Application> applications = applicationRepository.findByUserId(userId);
```

### Find Jobs by Skills
```java
List<Job> jobs = jobRepository.findByRequiredSkillsIn(Arrays.asList("Java", "Spring Boot"));
```

---

## Notes

- All timestamps use `LocalDateTime`
- All IDs are `String` type (MongoDB ObjectId as String)
- Embedded documents are used for nested structures
- Indexes should be created for frequently queried fields
- Consider adding TTL indexes for expired jobs if needed

