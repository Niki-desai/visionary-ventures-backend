# Spring Boot Guide for Node.js Developers

This guide will help you understand and run Spring Boot projects if you're coming from a Node.js background.

## 🎯 Key Differences: Node.js vs Spring Boot

| Node.js | Spring Boot |
|---------|-------------|
| JavaScript runtime | Java runtime (JVM) |
| `npm` / `yarn` (package manager) | `Maven` / `Gradle` (build tool) |
| `package.json` | `pom.xml` (Maven) or `build.gradle` (Gradle) |
| `node index.js` | `java -jar app.jar` or `mvn spring-boot:run` |
| `npm install` | `mvn install` or `mvnw install` |
| `npm start` | `mvn spring-boot:run` |
| Express.js / Fastify | Spring Boot Web |
| Mongoose | Spring Data MongoDB |

## 📦 What You Need to Install

### 1. **Java Development Kit (JDK) 17 or higher**

**Why?** Spring Boot runs on Java. This project requires Java 17.

**How to install:**
- Download from: https://adoptium.net/ (recommended) or https://www.oracle.com/java/technologies/downloads/
- Choose **JDK 17** (or JDK 21/25 if available)
- Install the Windows x64 installer
- During installation, check "Add to PATH" if available

**Verify installation:**
```powershell
java -version
```
You should see something like: `openjdk version "17.0.x"` or higher

**If Java is not found:**
- Find where Java was installed (usually `C:\Program Files\Java\jdk-17` or similar)
- Add to PATH manually:
  1. Search "Environment Variables" in Windows
  2. Click "Environment Variables"
  3. Under "System variables", find "Path" and click "Edit"
  4. Click "New" and add: `C:\Program Files\Java\jdk-17\bin`
  5. Create new variable `JAVA_HOME` = `C:\Program Files\Java\jdk-17`

### 2. **Maven (Optional - Project includes Maven Wrapper)**

**Why?** Maven is like `npm` for Java - it manages dependencies and builds your project.

**Good news:** This project includes `mvnw.cmd` (Maven Wrapper), so you don't need to install Maven separately! It will download Maven automatically on first use.

**If you want to install Maven anyway:**
- Download from: https://maven.apache.org/download.cgi
- Extract to a folder (e.g., `C:\Program Files\Apache\maven`)
- Add `C:\Program Files\Apache\maven\bin` to PATH

**Verify (if installed):**
```powershell
mvn -version
```

### 3. **MongoDB (Database)**

**Why?** This project uses MongoDB to store data.

**Option A: Local MongoDB**
- Download from: https://www.mongodb.com/try/download/community
- Install MongoDB Community Server
- MongoDB will run as a Windows service automatically

**Option B: MongoDB Atlas (Cloud - Easier for beginners)**
- Sign up at: https://www.mongodb.com/cloud/atlas
- Create a free cluster
- Get your connection string (looks like: `mongodb+srv://username:password@cluster.mongodb.net/`)

### 4. **IDE (Optional but Recommended)**

**IntelliJ IDEA Community Edition** (Free)
- Download: https://www.jetbrains.com/idea/download/
- Similar to VS Code but optimized for Java/Spring
- Automatically detects Spring Boot projects

**VS Code** (If you prefer)
- Install "Extension Pack for Java" extension
- Works but IntelliJ is better for Spring Boot

## 🚀 Step-by-Step: Running Your First Spring Boot Project

### Step 1: Verify Java is Installed

```powershell
java -version
```

If you see a version number, you're good! If not, install Java first (see above).

### Step 2: Set Up MongoDB

**If using local MongoDB:**
- Make sure MongoDB is running (check Windows Services or run `mongod`)

**If using MongoDB Atlas:**
- Copy your connection string
- You'll use it in Step 4

### Step 3: Configure Environment Variables (Optional)

Create a `.env` file in the project root (or set environment variables):

```bash
# MongoDB Configuration
MONGODB_URI=mongodb://localhost:27017/jobbot
# OR for Atlas: MONGODB_URI=mongodb+srv://user:pass@cluster.mongodb.net/jobbot

MONGODB_DATABASE=jobbot

# OpenAI Configuration (if using AI features)
OPENAI_API_KEY=your-api-key-here

# Server Port
SERVER_PORT=8080
```

**Note:** Spring Boot can also read from `application.yml` (already in the project), so environment variables are optional.

### Step 4: Run the Application

**Easiest Method - Using Maven Wrapper:**

```powershell
.\mvnw.cmd spring-boot:run
```

**If Java is not in PATH, set it first:**

```powershell
# Find your Java installation (common locations)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Then run
.\mvnw.cmd spring-boot:run
```

**Using the batch file (if it exists):**

```powershell
.\run.bat
```

**What happens:**
1. Maven wrapper downloads dependencies (first time only - like `npm install`)
2. Compiles Java code (like TypeScript compilation)
3. Starts the Spring Boot application
4. You'll see logs in the terminal

**Expected output:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

... (more logs) ...

Started JobBotApplication in X.XXX seconds
```

### Step 5: Test the Application

Open your browser and go to:
```
http://localhost:8080/api/health
```

Or use PowerShell:
```powershell
curl http://localhost:8080/api/health
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

### Step 6: Stop the Application

Press `Ctrl + C` in the terminal (just like Node.js)

## 📚 Common Commands (Node.js → Spring Boot)

| What you want to do | Node.js | Spring Boot |
|---------------------|---------|-------------|
| Install dependencies | `npm install` | `.\mvnw.cmd install` |
| Run application | `npm start` | `.\mvnw.cmd spring-boot:run` |
| Run tests | `npm test` | `.\mvnw.cmd test` |
| Build project | `npm run build` | `.\mvnw.cmd clean package` |
| Check dependencies | `npm list` | `.\mvnw.cmd dependency:tree` |
| Clean build | `rm -rf node_modules` | `.\mvnw.cmd clean` |

## 🏗️ Project Structure Explained

```
visionary-ventures-backend/
├── pom.xml                    # Like package.json (dependencies & config)
├── mvnw.cmd                   # Maven wrapper (like npx, but for Maven)
├── src/
│   └── main/
│       ├── java/              # Your Java source code
│       │   └── com/jobbot/
│       │       ├── JobBotApplication.java  # Main entry point (like index.js)
│       │       ├── controller/            # REST API endpoints (like Express routes)
│       │       ├── service/               # Business logic (like service files)
│       │       ├── repository/           # Database access (like Mongoose models)
│       │       ├── model/                # Data models (like TypeScript interfaces)
│       │       └── config/               # Configuration classes
│       └── resources/
│           └── application.yml           # Config file (like .env + config.js)
└── target/                    # Build output (like dist/ or build/)
```

## 🔍 Understanding Key Files

### `pom.xml` (Project Object Model)
- **Like:** `package.json`
- **Contains:** Dependencies, Java version, build configuration
- **Key sections:**
  - `<dependencies>` - Libraries your project uses (like `dependencies` in package.json)
  - `<properties>` - Java version, encoding, etc.
  - `<build>` - How to compile and package the app

### `application.yml`
- **Like:** `.env` + `config.js` combined
- **Contains:** Database connection, API keys, server port, etc.
- **Format:** YAML (indentation-sensitive, like Python)

### `JobBotApplication.java`
- **Like:** `index.js` or `app.js`
- **Contains:** Main method that starts the Spring Boot application
- **Annotation:** `@SpringBootApplication` tells Spring this is the main class

## 🐛 Troubleshooting

### "Java not found" or "javac is not recognized"
- Java is not installed or not in PATH
- Solution: Install Java 17+ and add to PATH (see Step 1)

### "MongoDB connection failed"
- MongoDB is not running
- Solution: Start MongoDB service or use MongoDB Atlas

### "Port 8080 already in use"
- Another application is using port 8080
- Solution: Change port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

### "Maven download is slow"
- First run downloads all dependencies (can be 100+ MB)
- Solution: Wait, or use a faster internet connection. Subsequent runs are faster.

### "ClassNotFoundException" or "NoClassDefFoundError"
- Dependencies not downloaded
- Solution: Run `.\mvnw.cmd clean install` first

## 💡 Tips for Node.js Developers

1. **Hot Reload:** Spring Boot DevTools (already in this project) provides hot reload like `nodemon`. Just save your file and the app restarts automatically.

2. **Dependency Injection:** Spring uses dependency injection (like NestJS). You'll see `@Autowired` or constructor injection.

3. **Annotations:** Spring uses annotations heavily (like decorators in TypeScript):
   - `@RestController` = Express router
   - `@Service` = Service class
   - `@Repository` = Database access layer
   - `@Autowired` = Dependency injection

4. **Compilation:** Java is compiled (like TypeScript), so you need to build before running. Spring Boot handles this automatically when you run `spring-boot:run`.

5. **Package Management:** Maven downloads dependencies to `~/.m2/repository` (like `node_modules` but global cache).

## 🎓 Next Steps

1. **Explore the code:**
   - Start with `JobBotApplication.java` (main entry point)
   - Check `controller/` for API endpoints
   - Look at `model/` for data structures

2. **Read Spring Boot docs:**
   - https://spring.io/guides
   - https://spring.io/projects/spring-boot

3. **Try modifying:**
   - Add a new endpoint in a controller
   - Create a new model class
   - The app will auto-reload with DevTools!

## 📝 Quick Reference Card

```powershell
# Check Java version
java -version

# Set Java path (if needed)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Install dependencies
.\mvnw.cmd install

# Run application
.\mvnw.cmd spring-boot:run

# Run tests
.\mvnw.cmd test

# Build JAR file
.\mvnw.cmd clean package

# Run JAR file
java -jar target/visionary-ventures-backend-1.0.0.jar
```

---

**Need help?** Check the project's `README.md` or `QUICK_START.md` for more specific instructions.

