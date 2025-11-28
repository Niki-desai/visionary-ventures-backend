# Spring Boot Core Concepts - Quick Reference

## 🎯 Essential Concepts

---

## 1️⃣ Dependency Injection (DI)

### What is it?
Instead of creating objects yourself, Spring creates and injects them for you.

### Express Way (Manual):
```javascript
// You create instances
const userService = new UserService();
const emailService = new EmailService();

class UserController {
    constructor() {
        this.userService = userService;  // Manual
        this.emailService = emailService; // Manual
    }
}
```

### Spring Way (Automatic):
```java
@RestController
public class UserController {
    
    @Autowired  // Spring injects automatically!
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    // No manual creation needed!
}
```

**Benefits:**
- ✅ No `new` keyword needed
- ✅ Single instances (Singleton pattern)
- ✅ Easy testing (mock injection)
- ✅ Loose coupling

---

## 2️⃣ Inversion of Control (IoC)

### What is it?
You don't control object creation — Spring does!

**Traditional way:**
```java
UserRepository repo = new UserRepository();  // You create
UserService service = new UserService(repo);  // You manage
```

**Spring way:**
```java
@Autowired
private UserService service;  // Spring creates and injects
```

**Container = Spring manages all objects (called "beans")**

---

## 3️⃣ Beans

### What is a Bean?
A **bean** is an object managed by Spring.

### How to create beans:

**Method 1: Annotations**
```java
@Service        // Creates a bean
@Repository     // Creates a bean
@Controller     // Creates a bean
@Component      // Creates a bean
```

**Method 2: @Bean method**
```java
@Configuration
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// Usage:
@Autowired
private PasswordEncoder passwordEncoder;  // Injected!
```

---

## 4️⃣ Stereotypes (Component Types)

```java
@Component      // Generic bean
    ↓
    ├── @Service       // Business logic
    ├── @Repository    // Database access
    └── @Controller    // Web layer
        └── @RestController  // REST API
```

**When to use:**
- `@Controller` / `@RestController` → Handle HTTP requests
- `@Service` → Business logic
- `@Repository` → Database operations
- `@Component` → Everything else

---

## 5️⃣ Component Scanning

### What is it?
Spring automatically finds and registers beans.

```java
@SpringBootApplication  // Scans current package and sub-packages
public class JobBotApplication {
    // Automatically finds all @Service, @Repository, @Controller
}
```

**Your project structure:**
```
com.jobbot/
├── JobBotApplication.java  ← Scans from here
├── controller/             ← Found!
├── service/                ← Found!
├── repository/             ← Found!
└── model/                  ← Found!
```

---

## 6️⃣ Auto-Configuration

### What is it?
Spring Boot automatically configures things based on dependencies.

**Example:**
- Add `spring-boot-starter-web` → Tomcat server auto-configured
- Add `spring-boot-starter-data-mongodb` → MongoDB auto-configured
- Add `spring-boot-starter-security` → Security auto-configured

**No manual setup needed!**

### Express equivalent:
```javascript
// You do this manually:
const express = require('express');
const mongoose = require('mongoose');
const app = express();

app.use(express.json());
mongoose.connect('mongodb://localhost:27017/db');
```

### Spring Boot:
```yaml
# Just add this in application.yml:
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/db

# Everything else is automatic!
```

---

## 7️⃣ Layered Architecture

### Your Project Structure:

```
┌─────────────────┐
│   Controller    │  ← HTTP Requests (Routes)
└────────┬────────┘
         │
┌────────▼────────┐
│     Service     │  ← Business Logic
└────────┬────────┘
         │
┌────────▼────────┐
│   Repository    │  ← Database Operations
└────────┬────────┘
         │
┌────────▼────────┐
│     MongoDB     │  ← Database
└─────────────────┘
```

**Express pattern:**
```
Routes → Controller → Model → Database
```

**Spring Boot pattern:**
```
Controller → Service → Repository → Database
```

### Example Flow:

**Request:**
```
GET /api/users/123
```

**1. Controller:**
```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable String id) {
    return ResponseEntity.ok(userService.getUser(id));
}
```

**2. Service:**
```java
@Service
public class UserService {
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow();
    }
}
```

**3. Repository:**
```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // findById() auto-implemented!
}
```

**4. Database:**
```
MongoDB query: db.users.findOne({ _id: ObjectId("123") })
```

---

## 8️⃣ Properties/Configuration

### Express (.env):
```bash
PORT=3000
DB_URL=mongodb://localhost:27017
JWT_SECRET=secret
```

```javascript
require('dotenv').config();
const port = process.env.PORT;
```

### Spring Boot (application.yml):
```yaml
server:
  port: ${PORT:8080}  # Default: 8080

spring:
  data:
    mongodb:
      uri: ${DB_URL:mongodb://localhost:27017}

jwt:
  secret: ${JWT_SECRET:default}
```

```java
@Value("${jwt.secret}")
private String jwtSecret;
```

**Features:**
- Default values: `${VAR:default}`
- Type conversion: Automatic!
- Validation: Built-in

---

## 9️⃣ Profiles (Environments)

### Express:
```javascript
// Different .env files
// .env.development
// .env.production

if (process.env.NODE_ENV === 'production') {
    // Production config
}
```

### Spring Boot:
```yaml
# application.yml (common)
# application-dev.yml (development)
# application-prod.yml (production)

# Run with:
# --spring.profiles.active=dev
# --spring.profiles.active=prod
```

```java
@Profile("dev")
@Service
public class DevService { }  // Only active in dev

@Profile("prod")
@Service
public class ProdService { }  // Only active in prod
```

---

## 🔟 Exception Handling

### Express:
```javascript
app.use((err, req, res, next) => {
    res.status(500).json({ error: err.message });
});
```

### Spring Boot:
```java
@ControllerAdvice  // Global exception handler
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}
```

**One global handler for all controllers!**

---

## 🔒 Security

### Express + Passport:
```javascript
const passport = require('passport');
const jwt = require('jsonwebtoken');

// Middleware
const authMiddleware = (req, res, next) => {
    const token = req.headers.authorization;
    jwt.verify(token, SECRET, (err, decoded) => {
        if (err) return res.status(401).json({ error: 'Unauthorized' });
        req.user = decoded;
        next();
    });
};

app.use('/api/protected', authMiddleware);
```

### Spring Boot + Security:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

**Plus JWT Filter for token validation.**

---

## 📦 Packaging

### Express:
```bash
# No compilation needed
node index.js  # Run directly
```

### Spring Boot:
```bash
mvn package  # Creates JAR file
java -jar myapp.jar  # Run anywhere
```

**Difference:**
- Express: Interpreted (no build)
- Spring Boot: Compiled (build required)

---

## 🎓 Learning Path for Express Developers

### Week 1: Basics
- ✅ Understand annotations
- ✅ Create REST controllers
- ✅ Use repositories
- ✅ Practice CRUD operations

### Week 2: Intermediate
- ✅ Dependency injection
- ✅ Service layer pattern
- ✅ Exception handling
- ✅ Validation

### Week 3: Advanced
- ✅ Spring Security
- ✅ JWT authentication
- ✅ Custom queries
- ✅ Transaction management

---

## 🔥 Common Gotchas for Node Developers

### 1. **No `async/await` needed!**
```javascript
// Express (async required)
app.get('/users', async (req, res) => {
    const users = await User.find();
    res.json(users);
});
```

```java
// Spring Boot (sync works fine)
@GetMapping("/users")
public List<User> getUsers() {
    return userRepository.findAll();  // No async!
}
```

**Why?** Java handles threading differently. Spring uses thread pools.

---

### 2. **Getters/Setters required!**
```javascript
// Express (direct access)
const user = { email: 'test@example.com' };
console.log(user.email);  // Works!
```

```java
// Spring Boot (use getters/setters)
User user = new User();
user.setEmail("test@example.com");  // Setter
System.out.println(user.getEmail());  // Getter
```

**Tip:** Use Lombok `@Data` annotation to auto-generate getters/setters!

---

### 3. **Interface for Repository!**
```javascript
// Express (class)
class UserRepository {
    async findAll() { }
}
```

```java
// Spring Boot (interface!)
public interface UserRepository extends MongoRepository<User, String> {
    // No implementation needed!
}
```

---

### 4. **Compilation errors vs Runtime errors**
```javascript
// Express (runtime error)
const user = { email: 123 };  // Wrong type, but runs
```

```java
// Spring Boot (compile error)
User user = new User();
user.setEmail(123);  // ❌ Compile error! Email is String
```

**Benefit:** Catch errors before running!

---

## 🎯 Quick Wins

### 1. Auto-Reload (like nodemon):
```bash
# Add to pom.xml (already done!)
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>

# Changes auto-reload!
```

### 2. API Docs (like FastAPI):
```bash
# Add Swagger (already done!)
# Open: http://localhost:8080/swagger-ui.html
# Interactive API testing!
```

### 3. Environment Config:
```yaml
# application.yml (like .env)
server:
  port: 8080
```

---

## 💡 Mental Model Mapping

| Express Concept | Spring Boot Equivalent | Your Project Example |
|----------------|------------------------|---------------------|
| Router | Controller | `UserController.java` |
| Middleware | Filter/Interceptor | `JwtFilter.java` |
| Service Class | Service | `UserService.java` |
| Mongoose Model | Entity + Repository | `User.java` + `UserRepository.java` |
| `app.listen()` | `@SpringBootApplication` | `JobBotApplication.java` |
| `express.json()` | Auto-configured | Not needed! |
| Error handler | `@ControllerAdvice` | `GlobalExceptionHandler.java` |
| `dotenv` | `application.yml` | Config files |
| `npm install` | `mvn install` | Dependency management |

---

## 🚀 Your Next Steps

1. **Read your Controllers** - See how they map to Express routes
2. **Check Repositories** - Understand auto-generated queries
3. **Follow request flow** - Controller → Service → Repository
4. **Try Swagger UI** - Test APIs interactively
5. **Make changes** - DevTools will auto-reload!

---

## 📚 Resources

- **Official Docs:** https://spring.io/projects/spring-boot
- **Spring Data MongoDB:** https://spring.io/projects/spring-data-mongodb
- **Baeldung (Best tutorials):** https://www.baeldung.com/spring-boot

---

**You already know Express. Spring Boot is just different syntax for same concepts! 🎯**

**Key mindset:** 
- Express = You configure everything
- Spring Boot = Conventions + auto-configuration

**Happy learning! 🚀**

