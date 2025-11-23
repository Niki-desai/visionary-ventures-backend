# Quick Start Commands

## To Run the Application

### Option 1: PowerShell (One Line)
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"; .\mvnw.cmd spring-boot:run
```

### Option 2: PowerShell (Step by Step)
```powershell
# Set Java Path
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"

# Run Application
.\mvnw.cmd spring-boot:run
```

### Option 3: Using Batch File
```powershell
.\run.bat
```

### Option 4: Git Bash (if Java works there)
```bash
./mvnw spring-boot:run
```

## To Stop the Application
Press `Ctrl + C` in the terminal

## To Test the Application
Once running, open in browser:
```
http://localhost:8080/api/health
```

Or use curl:
```powershell
curl http://localhost:8080/api/health
```

