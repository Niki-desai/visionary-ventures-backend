# Quick Start Guide

## Option 1: Using Batch File (Easiest)

Simply double-click `run.bat` or run in PowerShell:
```powershell
.\run.bat
```

## Option 2: Using Maven Wrapper Directly

### Step 1: Set JAVA_HOME (if not set)

Find your Java installation path. Common locations:
- `C:\Program Files\Java\jdk-25`
- `C:\Program Files\Java\jdk-25.0.1`

Then set it temporarily in PowerShell:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

### Step 2: Run the Application

```powershell
.\mvnw.cmd spring-boot:run
```

## Option 3: If Java is in PATH in Git Bash

If `java -version` works in Git Bash, you can run:
```bash
./mvnw spring-boot:run
```

## Option 4: Using IDE (IntelliJ IDEA / Eclipse)

1. Open the project in your IDE
2. Wait for Maven to download dependencies
3. Right-click on `JobBotApplication.java`
4. Select "Run" or "Debug"

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

