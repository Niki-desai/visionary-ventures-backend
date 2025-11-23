# Correct Run Command

## ✅ Correct Command:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"; .\mvnw.cmd spring-boot:run
```

## ❌ Wrong (What you typed):

```powershell
.\mvnw.cmd spring-boot:runAPI  # ❌ Wrong - "runAPI" doesn't exist
```

## 📝 Step by Step:

**Step 1: Set Java Path**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"
```

**Step 2: Run Application**
```powershell
.\mvnw.cmd spring-boot:run
```

## 🚀 Or Use Batch File:

```powershell
.\run.bat
```

---

**Note:** The correct goal is `spring-boot:run` (not `runAPI`)

