# Development Guide - Running, Hot Reload & Quick Start

## 📚 Table of Contents

1. [Quick Start](#quick-start)
2. [Running the Application](#running-the-application)
3. [Hot Reload Setup](#hot-reload-setup)
4. [Troubleshooting](#troubleshooting)

---

## Quick Start

### Option 1: Using Batch File (Easiest)

Simply double-click `run.bat` or run in PowerShell:
```powershell
.\run.bat
```

### Option 2: Using Maven Wrapper Directly

#### Step 1: Set JAVA_HOME (if not set)

Find your Java installation path. Common locations:
- `C:\Program Files\Java\jdk-25`
- `C:\Program Files\Java\jdk-25.0.1`

Then set it temporarily in PowerShell:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

#### Step 2: Run the Application

```powershell
.\mvnw.cmd spring-boot:run
```

### Option 3: If Java is in PATH in Git Bash

If `java -version` works in Git Bash, you can run:
```bash
./mvnw spring-boot:run
```

### Option 4: Using IDE (IntelliJ IDEA / Eclipse)

1. Open the project in your IDE
2. Wait for Maven to download dependencies
3. Right-click on `JobBotApplication.java`
4. Select "Run" or "Debug"

---

## Running the Application

### ✅ Correct Command:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"; .\mvnw.cmd spring-boot:run
```

### ❌ Wrong (What you might type):

```powershell
.\mvnw.cmd spring-boot:runAPI  # ❌ Wrong - "runAPI" doesn't exist
```

### 📝 Step by Step:

**Step 1: Set Java Path**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"
```

**Step 2: Run Application**
```powershell
.\mvnw.cmd spring-boot:run
```

### 🚀 Or Use Batch File:

```powershell
.\run.bat
```

**Note:** The correct goal is `spring-boot:run` (not `runAPI`)

---

## Verify It's Running

Once started, open your browser and go to:
```
http://localhost:8080/api/health
```

You should see:
```json
{
  "status": "UP",
  "timestamp": "...",
  "service": "Visionary Ventures Backend",
  "version": "1.0.0"
}
```

---

## Hot Reload Setup - No Restart Needed! 🔥

### ✅ Spring Boot DevTools Already Included!

Your project already has **Spring Boot DevTools** which enables hot reload. Just need to configure it properly.

---

## 🚀 Quick Setup

### Option 1: IDE Auto-Compile (Recommended)

#### IntelliJ IDEA:
1. **Enable Auto-Compile:**
   - File → Settings → Build, Execution, Deployment → Compiler
   - Check: ✅ "Build project automatically"
   - Check: ✅ "Compile independent modules in parallel"

2. **Enable Auto-Reload:**
   - File → Settings → Advanced Settings
   - Check: ✅ "Allow auto-make to start even if developed application is currently running"

3. **Registry Setting:**
   - Press `Ctrl + Shift + A` (or `Cmd + Shift + A` on Mac)
   - Type: `Registry`
   - Enable: `compiler.automake.allow.when.app.running`

#### VS Code:
1. Install Extension: **Spring Boot Extension Pack**
2. Auto-compile is enabled by default
3. Changes will auto-reload

#### Eclipse:
1. Project → Build Automatically (check it)
2. DevTools will auto-reload

---

## 🔧 Manual Configuration

### 1. Run with Dev Profile:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Or Set in application.yml:
```yaml
spring:
  profiles:
    active: dev
```

---

## 📝 What Gets Auto-Reloaded:

✅ **Java Classes** - Controllers, Services, Models
✅ **Configuration Files** - application.yml changes
✅ **Resources** - Static files, templates
✅ **Properties** - All property changes

❌ **Not Reloaded:**
- Dependencies (pom.xml changes)
- Database schema changes
- Port changes

---

## 🎯 How It Works:

1. **You make changes** in code
2. **IDE auto-compiles** (if enabled)
3. **DevTools detects** class changes
4. **Application restarts** automatically (fast restart, not full restart)
5. **Changes applied** - No manual restart needed!

---

## ⚡ Fast Restart vs Full Restart:

- **Fast Restart**: Only reloads changed classes (2-3 seconds)
- **Full Restart**: Complete application restart (10-20 seconds)

DevTools uses **Fast Restart** by default!

---

## 🔍 Verify It's Working:

1. Start application
2. Make a small change (e.g., change a log message)
3. Save the file
4. Watch console - you'll see:
   ```
   Restarting due to changes...
   ```

---

## 🛠️ Troubleshooting

### If Auto-Reload Not Working:

1. **Check DevTools is in dependencies:**
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-devtools</artifactId>
   </dependency>
   ```
   ✅ Already included!

2. **Check IDE Auto-Compile:**
   - Make sure IDE is compiling automatically
   - Check build output for compilation

3. **Manual Trigger:**
   - In IntelliJ: Build → Rebuild Project
   - In VS Code: Save file (Ctrl+S)

4. **Check Exclusions:**
   - DevTools excludes: `static/**`, `public/**`, `templates/**`
   - These need manual restart

---

## 💡 Pro Tips:

### 1. Use LiveReload Browser Extension:
- Install: **LiveReload** browser extension
- Changes in HTML/CSS will auto-refresh browser!

### 2. Disable for Production:
```yaml
spring:
  devtools:
    restart:
      enabled: false  # In production
```

### 3. Customize Exclusions:
```yaml
spring:
  devtools:
    restart:
      exclude: static/**,public/**,templates/**,config/**
```

---

## 🎬 Example Workflow:

1. **Start app once:**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

2. **Make changes:**
   - Edit `AuthController.java`
   - Add new endpoint
   - Save file

3. **Watch console:**
   ```
   Restarting due to changes in classpath...
   ```

4. **Test immediately:**
   - No restart needed!
   - Changes are live!

---

## 📊 Performance:

- **First Start**: ~10-15 seconds
- **Fast Restart**: ~2-3 seconds
- **Full Restart**: ~10-20 seconds

With DevTools: **2-3 seconds** for most changes! ⚡

---

## Troubleshooting

### Java Not Found
- Add Java to your system PATH permanently:
  1. Search "Environment Variables" in Windows
  2. Edit "Path" variable
  3. Add: `C:\Program Files\Java\jdk-25\bin`
  4. Create new variable `JAVA_HOME` = `C:\Program Files\Java\jdk-25`

### Port 8080 Already in Use
- Change port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

### MongoDB Connection Error
- Make sure MongoDB is running
- Or use MongoDB Atlas (cloud) and update connection string in `application.yml`

---

## ✅ Summary:

1. ✅ DevTools already in dependencies
2. ✅ Enable IDE auto-compile
3. ✅ Save files → Auto-reload happens
4. ✅ No manual restart needed!

**Just code, save, and it reloads automatically! 🚀**

