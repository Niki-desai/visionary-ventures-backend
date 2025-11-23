# MongoDB Data Seeding Guide

## How to Seed Data into MongoDB

### Option 1: Automatic Seeding on Startup

1. Open `DataSeederService.java`
2. Uncomment the `seedData()` call in the `run()` method:
   ```java
   @Override
   public void run(String... args) {
       seedData(); // Uncomment this line
   }
   ```
3. Run the application - data will be seeded automatically

### Option 2: Manual Seeding via API (Recommended)

Create a controller endpoint to trigger seeding:

```java
@PostMapping("/api/admin/seed")
public ResponseEntity<String> seedData() {
    dataSeederService.seedData();
    return ResponseEntity.ok("Data seeded successfully");
}
```

### Option 3: MongoDB Script

You can also use MongoDB shell scripts or MongoDB Compass to insert data directly.

---

## What Gets Seeded

### Sample Data Created:

1. **1 User**
   - Email: `demo@jobbot.com`
   - Name: John Doe
   - Subscription: PREMIUM
   - Preferences: Job alerts enabled, auto-apply enabled

2. **2 Jobs**
   - Senior Software Engineer (Tech Corp, SF)
   - Full Stack Developer (StartupXYZ, NY)

3. **1 Resume**
   - Default resume for the user
   - Includes experience, education, skills

4. **1 Job Search**
   - Saved search: "Software Engineer Jobs - SF/NY"
   - Active with daily alerts

5. **1 Application**
   - Application to the Senior Software Engineer job
   - Status: submitted
   - Includes AI insights

6. **1 AI Conversation**
   - Sample conversation about job search
   - Includes message history

---

## Indexes

Indexes are automatically created when the application starts via `MongoIndexConfig.java`.

### Indexes Created:

- **users**: email (unique)
- **jobs**: title/description (text), companyId, jobType, industry, remoteType, etc.
- **applications**: userId+jobId (compound), userId+status
- **resumes**: userId+isDefault (compound)
- **job_searches**: userId+isActive (compound)
- **ai_conversations**: userId+conversationType (compound)

---

## Verify Data

### Using MongoDB Compass:
1. Connect to `mongodb://localhost:27017`
2. Select database: `jobbot`
3. Check collections: `users`, `jobs`, `applications`, etc.

### Using MongoDB Shell:
```bash
mongosh
use jobbot
db.users.find()
db.jobs.find()
db.applications.find()
```

### Using Spring Boot:
```java
@Autowired
private UserRepository userRepository;

public void checkData() {
    List<User> users = userRepository.findAll();
    System.out.println("Users: " + users.size());
}
```

---

## Clear All Data

⚠️ **Warning**: This will delete all data!

In `DataSeederService.java`, uncomment the `clearAllData()` call in `seedData()`:

```java
public void seedData() {
    clearAllData(); // Uncomment to clear existing data
    // ... rest of seeding code
}
```

---

## Customize Sample Data

Edit the methods in `DataSeederService.java`:
- `createSampleUser()` - Modify user data
- `createSampleJobs()` - Add more jobs or change details
- `createSampleResume()` - Customize resume
- `createSampleJobSearch()` - Change search criteria
- `createSampleApplication()` - Modify application
- `createSampleAIConversation()` - Change conversation

---

## Production Notes

- **Never** enable automatic seeding in production
- Use database migrations for production data
- Always backup before seeding
- Test seeding in development/staging first

