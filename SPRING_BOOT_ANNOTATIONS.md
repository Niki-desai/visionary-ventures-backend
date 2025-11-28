# Spring Boot Annotations - Complete Reference

## 🎯 What are Annotations?

Annotations are like **decorators** in TypeScript or **attributes** in Python. They add metadata and behavior to your code.

```java
@RestController  // ← This is an annotation
public class UserController {
    // ...
}
```

Think of them as **instructions to Spring** about how to handle your code.

---

## 📦 Core Spring Boot Annotations

### 1. `@SpringBootApplication`
**Where:** Main application class  
**What it does:** Combines three annotations:
- `@Configuration` - Marks as configuration source
- `@EnableAutoConfiguration` - Auto-configures Spring
- `@ComponentScan` - Scans for components

```java
@SpringBootApplication
public class JobBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobBotApplication.class, args);
    }
}
```

**Express equivalent:** Your main `app.js` file

---

## 🌐 Web/Controller Annotations

### 2. `@RestController`
**Where:** Controller classes  
**What it does:** Marks class as REST API controller + auto-converts responses to JSON

```java
@RestController
public class UserController {
    @GetMapping("/users")
    public List<User> getUsers() {
        return users; // Auto-converted to JSON
    }
}
```

**Express equivalent:**
```javascript
app.get('/users', (req, res) => {
    res.json(users);
});
```

### 3. `@Controller`
**Where:** MVC controllers  
**What it does:** Marks as web controller (returns views, not JSON)

**Use `@RestController` for APIs, `@Controller` for web pages.**

---

### 4. `@RequestMapping`
**Where:** Class or method level  
**What it does:** Maps HTTP requests to methods

```java
@RestController
@RequestMapping("/api/users")  // Base path
public class UserController {
    
    @RequestMapping(method = RequestMethod.GET)  // /api/users
    public List<User> getUsers() { }
}
```

**Express equivalent:**
```javascript
const router = express.Router();
router.route('/api/users')
    .get((req, res) => { });
```

---

### 5-9. HTTP Method Annotations

```java
@GetMapping("/users")           // GET request
@PostMapping("/users")          // POST request
@PutMapping("/users/{id}")      // PUT request
@PatchMapping("/users/{id}")    // PATCH request
@DeleteMapping("/users/{id}")   // DELETE request
```

**Express equivalent:**
```javascript
router.get('/users', ...)
router.post('/users', ...)
router.put('/users/:id', ...)
router.patch('/users/:id', ...)
router.delete('/users/:id', ...)
```

---

## 📥 Request Parameter Annotations

### 10. `@RequestBody`
**What it does:** Maps request body to object

```java
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    // user contains request body
}
```

**Express equivalent:**
```javascript
app.post('/users', (req, res) => {
    const user = req.body;
});
```

---

### 11. `@PathVariable`
**What it does:** Extracts path variables

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable String id) {
    // id from URL path
}
```

**Express equivalent:**
```javascript
app.get('/users/:id', (req, res) => {
    const id = req.params.id;
});
```

---

### 12. `@RequestParam`
**What it does:** Extracts query parameters

```java
@GetMapping("/users")
public List<User> searchUsers(@RequestParam String name) {
    // /users?name=John
}
```

**Express equivalent:**
```javascript
app.get('/users', (req, res) => {
    const name = req.query.name;
});
```

**Advanced usage:**
```java
@RequestParam(required = false) String name           // Optional
@RequestParam(defaultValue = "10") int limit          // Default value
@RequestParam(value = "user_name") String userName    // Different param name
```

---

### 13. `@RequestHeader`
**What it does:** Extracts header values

```java
@GetMapping("/users")
public List<User> getUsers(@RequestHeader("Authorization") String token) {
    // token from header
}
```

**Express equivalent:**
```javascript
app.get('/users', (req, res) => {
    const token = req.headers.authorization;
});
```

---

## 🧩 Service/Component Annotations

### 14. `@Service`
**Where:** Service layer classes  
**What it does:** Marks class as service (business logic)

```java
@Service
public class UserService {
    public User createUser(User user) {
        // Business logic
    }
}
```

**Express equivalent:**
```javascript
class UserService {
    createUser(user) {
        // Business logic
    }
}
module.exports = new UserService();
```

---

### 15. `@Repository`
**Where:** Data access layer  
**What it does:** Marks as database repository + adds DB exception translation

```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Auto-implemented by Spring!
}
```

**Express equivalent:**
```javascript
class UserRepository {
    async findAll() {
        return await User.find();
    }
}
```

---

### 16. `@Component`
**Where:** Generic Spring components  
**What it does:** Marks class as Spring-managed component

```java
@Component
public class EmailHelper {
    public void sendEmail() { }
}
```

**Use:**
- `@Service` for business logic
- `@Repository` for database
- `@Component` for everything else

---

## 💉 Dependency Injection Annotations

### 17. `@Autowired`
**What it does:** Automatic dependency injection

```java
@Service
public class UserService {
    
    @Autowired  // Spring auto-injects!
    private UserRepository userRepository;
}
```

**Express equivalent:**
```javascript
const UserRepository = require('./UserRepository');
const userRepo = new UserRepository();  // Manual
```

**Three ways to use:**

**1. Field injection (most common):**
```java
@Autowired
private UserService userService;
```

**2. Constructor injection (recommended):**
```java
private final UserService userService;

@Autowired
public UserController(UserService userService) {
    this.userService = userService;
}
```

**3. Setter injection:**
```java
private UserService userService;

@Autowired
public void setUserService(UserService userService) {
    this.userService = userService;
}
```

---

## ⚙️ Configuration Annotations

### 18. `@Configuration`
**Where:** Configuration classes  
**What it does:** Marks as configuration source

```java
@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### 19. `@Bean`
**Where:** Inside `@Configuration` class  
**What it does:** Creates and manages bean instances

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// Usage:
@Autowired
private PasswordEncoder passwordEncoder;  // Auto-injected!
```

---

### 20. `@Value`
**What it does:** Injects values from properties/environment

```java
@Value("${jwt.secret}")
private String jwtSecret;

@Value("${server.port:8080}")  // Default value
private int port;
```

**Express equivalent:**
```javascript
const jwtSecret = process.env.JWT_SECRET;
```

---

## 🗄️ Database/MongoDB Annotations

### 21. `@Document`
**What it does:** Marks class as MongoDB document

```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
}
```

**Mongoose equivalent:**
```javascript
const userSchema = new mongoose.Schema({
    email: String
});
const User = mongoose.model('User', userSchema);
```

---

### 22. `@Id`
**What it does:** Marks field as primary key

```java
@Id
private String id;  // MongoDB's _id
```

**Mongoose equivalent:**
```javascript
// _id is automatic in Mongoose
```

---

### 23. `@Field`
**What it does:** Maps field to database column name

```java
@Field("email_address")
private String email;  // Stored as "email_address" in DB
```

**Mongoose equivalent:**
```javascript
const schema = new mongoose.Schema({
    email: { type: String, field: 'email_address' }
});
```

---

### 24. `@Indexed`
**What it does:** Creates index on field

```java
@Indexed(unique = true)
private String email;
```

**Mongoose equivalent:**
```javascript
email: { type: String, unique: true, index: true }
```

---

## 🔒 Validation Annotations

### 25. `@Valid`
**What it does:** Triggers validation on object

```java
@PostMapping("/users")
public User createUser(@Valid @RequestBody User user) {
    // Validates user before method runs
}
```

---

### 26-32. Validation Constraints

```java
import jakarta.validation.constraints.*;

public class User {
    @NotNull
    private String name;
    
    @NotBlank                    // Not null, not empty
    private String firstName;
    
    @Email                       // Valid email format
    private String email;
    
    @Size(min = 6, max = 20)     // String length
    private String password;
    
    @Min(18)                     // Minimum value
    @Max(100)                    // Maximum value
    private int age;
    
    @Pattern(regexp = "\\d{10}") // Regex pattern
    private String phone;
}
```

**Express equivalent:**
```javascript
const { body, validationResult } = require('express-validator');

app.post('/users', [
    body('email').isEmail(),
    body('password').isLength({ min: 6 })
], (req, res) => {
    const errors = validationResult(req);
});
```

---

## 🔐 Security Annotations

### 33. `@EnableWebSecurity`
**What it does:** Enables Spring Security

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Security configuration
}
```

---

## 📚 Swagger/Documentation Annotations

### 34. `@Tag`
**What it does:** Groups endpoints in Swagger UI

```java
@RestController
@Tag(name = "Users", description = "User management endpoints")
public class UserController { }
```

---

### 35. `@Operation`
**What it does:** Documents endpoint

```java
@Operation(
    summary = "Get user by ID",
    description = "Returns a single user"
)
@GetMapping("/{id}")
public User getUser(@PathVariable String id) { }
```

---

### 36. `@Schema`
**What it does:** Documents model properties

```java
public class User {
    @Schema(description = "User email", example = "user@example.com")
    private String email;
}
```

---

## 🔄 Async/Transaction Annotations

### 37. `@Async`
**What it does:** Runs method asynchronously

```java
@Async
public void sendEmail(String to) {
    // Runs in background thread
}
```

---

### 38. `@Transactional`
**What it does:** Wraps method in database transaction

```java
@Transactional
public void transferMoney(String from, String to, double amount) {
    // All DB operations succeed or all fail
}
```

---

## 🎯 Annotation Cheat Sheet

| Annotation | Level | Purpose | Express Equivalent |
|------------|-------|---------|-------------------|
| `@SpringBootApplication` | Class | Main app | `app.listen()` |
| `@RestController` | Class | REST API | `express.Router()` |
| `@GetMapping` | Method | GET request | `router.get()` |
| `@PostMapping` | Method | POST request | `router.post()` |
| `@RequestBody` | Parameter | Request body | `req.body` |
| `@PathVariable` | Parameter | URL param | `req.params.id` |
| `@RequestParam` | Parameter | Query param | `req.query.name` |
| `@Service` | Class | Business logic | Service class |
| `@Repository` | Interface | Database | Mongoose model |
| `@Autowired` | Field/Constructor | Dependency injection | `require()` |
| `@Value` | Field | Environment var | `process.env.VAR` |
| `@Document` | Class | MongoDB doc | `mongoose.model()` |

---

## 💡 Pro Tips:

1. **Annotations are metadata** - They don't execute code, they tell Spring what to do

2. **Order matters sometimes** - Put `@RestController` before method-level annotations

3. **Combine annotations** - You can use multiple on same element

4. **IDE help** - IntelliJ/VS Code show annotation hints

5. **Read your code** - Annotations make code self-documenting

---

**Remember:** Annotations = Instructions to Spring Framework! 🎯

