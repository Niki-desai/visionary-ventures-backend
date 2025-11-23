# MongoDB Setup Guide

## Issue: Collections/Tables Not Showing

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

