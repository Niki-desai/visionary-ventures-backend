# Hot Reload Setup - No Restart Needed! 🔥

## ✅ Spring Boot DevTools Already Included!

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

## ✅ Summary:

1. ✅ DevTools already in dependencies
2. ✅ Enable IDE auto-compile
3. ✅ Save files → Auto-reload happens
4. ✅ No manual restart needed!

**Just code, save, and it reloads automatically! 🚀**

