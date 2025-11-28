# Spring Boot for Node.js/Express Developers

## 🎯 Quick Start Guide for Express Developers

If you know **Express.js**, you'll understand Spring Boot quickly! This guide maps Express concepts to Spring Boot.

---

## 📦 Project Setup Comparison

### Express (Node.js):
```javascript
// package.json
{
  "name": "my-app",
  "dependencies": {
    "express": "^4.18.0",
    "mongoose": "^6.0.0"
  }
}

// npm install
```

### Spring Boot (Java):
```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>

<!-- Maven automatically downloads -->
```

**Key Difference:** 
- Express: `npm install` + `package.json`
- Spring Boot: `mvn install` + `pom.xml`

---

## 🚀 Application Entry Point

### Express:
```javascript
// index.js
const express = require('express');
const app = express();

app.use(express.json());

app.listen(3000, () => {
  console.log('Server running on port 3000');
});
```

### Spring Boot:
```java
// JobBotApplication.java
@SpringBootApplication
public class JobBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobBotApplication.class, args);
    }
}
```

**Key Difference:**
- Express: Manual setup with `app.listen()`
- Spring Boot: `@SpringBootApplication` auto-configures everything!

---

## 🛣️ Routing Comparison

### Express Routes:
```javascript
// userRoutes.js
const express = require('express');
const router = express.Router();

// GET /api/users
router.get('/api/users', (req, res) => {
  res.json({ users: [] });
});

// POST /api/users
router.post('/api/users', (req, res) => {
  const user = req.body;
  res.json({ success: true });
});

module.exports = router;
```

### Spring Boot Controller:
```java
// UserController.java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // GET /api/users
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }
    
    // POST /api/users
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }
}
```

**Mapping:**
- `router.get()` = `@GetMapping`
- `router.post()` = `@PostMapping`
- `router.put()` = `@PutMapping`
- `router.delete()` = `@DeleteMapping`
- `req.body` = `@RequestBody`
- `req.params.id` = `@PathVariable`
- `req.query` = `@RequestParam`

---

## 📊 Database/ORM Comparison

### Express + Mongoose (MongoDB):
```javascript
// User.js (Model)
const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  email: { type: String, unique: true },
  name: String,
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('User', userSchema);
```

### Spring Boot + MongoDB:
```java
// User.java (Model)
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String name;
    
    private LocalDateTime createdAt;
    
    // Getters and Setters
}
```

**Key Concepts:**
- `mongoose.Schema` = Java Class with `@Document`
- `mongoose.model()` = Not needed! Spring auto-creates
- `unique: true` = `@Indexed(unique = true)`

---

## 🔄 Database Operations

### Express + Mongoose:
```javascript
// userService.js
const User = require('./User');

// Find all
const users = await User.find();

// Find by ID
const user = await User.findById(id);

// Create
const newUser = await User.create({ email, name });

// Update
await User.findByIdAndUpdate(id, { name });

// Delete
await User.findByIdAndDelete(id);
```

### Spring Boot + MongoDB:
```java
// UserRepository.java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring auto-implements these!
    List<User> findAll();
    Optional<User> findById(String id);
    User save(User user);
    void deleteById(String id);
    
    // Custom queries
    Optional<User> findByEmail(String email);
}

// Usage in Service:
@Autowired
private UserRepository userRepository;

List<User> users = userRepository.findAll();
```

**Magic of Spring Data:**
- Just create **interface** (not class!)
- Spring **automatically implements** all methods!
- Just write method names like `findByEmail`, Spring generates query!

---

## 🧩 Service Layer

### Express:
```javascript
// userService.js
class UserService {
  async createUser(data) {
    const user = await User.create(data);
    return user;
  }
  
  async getUser(id) {
    return await User.findById(id);
  }
}

module.exports = new UserService();
```

### Spring Boot:
```java
// UserService.java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
```

**Key Concepts:**
- `@Service` = Marks as service layer
- `@Autowired` = Automatic dependency injection (like `require()` but automatic!)

---

## 🔐 Middleware vs Filters/Interceptors

### Express Middleware:
```javascript
// authMiddleware.js
const authMiddleware = (req, res, next) => {
  const token = req.headers.authorization;
  if (!token) {
    return res.status(401).json({ error: 'Unauthorized' });
  }
  // Verify token
  next();
};

app.use('/api/protected', authMiddleware);
```

### Spring Boot Filter:
```java
// JwtFilter.java
@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            response.setStatus(401);
            return;
        }
        // Verify token
        filterChain.doFilter(request, response);
    }
}
```

**Mapping:**
- `app.use()` = Filters in Spring
- `next()` = `filterChain.doFilter()`

---

## 🔒 Environment Variables

### Express (.env):
```bash
PORT=3000
MONGODB_URI=mongodb://localhost:27017/mydb
JWT_SECRET=my-secret-key
```

```javascript
// Usage
require('dotenv').config();
const port = process.env.PORT;
```

### Spring Boot (application.yml):
```yaml
server:
  port: ${PORT:8080}

spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/mydb}

jwt:
  secret: ${JWT_SECRET:default-secret}
```

```java
// Usage
@Value("${jwt.secret}")
private String jwtSecret;
```

**Key Difference:**
- Express: `.env` + `dotenv` package
- Spring Boot: `application.yml` + `@Value` annotation

---

## 📝 Key Spring Boot Annotations Explained

### 1. Class-Level Annotations:

```java
@SpringBootApplication  // Main app entry point (like app.listen())
@RestController         // REST API controller (like Express router)
@Service                // Business logic layer
@Repository             // Database access layer
@Configuration          // Configuration class
@Component              // Generic Spring-managed component
```

### 2. Request Mapping:

```java
@RequestMapping("/api/users")  // Base path (like router.route())
@GetMapping                    // GET request (like router.get())
@PostMapping                   // POST request (like router.post())
@PutMapping                    // PUT request
@DeleteMapping                 // DELETE request
@PatchMapping                  // PATCH request
```

### 3. Request Parameters:

```java
@RequestBody User user         // req.body in Express
@PathVariable String id        // req.params.id
@RequestParam String name      // req.query.name
@RequestHeader String token    // req.headers.token
```

### 4. Dependency Injection:

```java
@Autowired                     // Auto-inject dependencies (like require())
@Value("${property}")          // Inject environment variable
```

### 5. Database:

```java
@Document(collection = "users")  // Mongoose model
@Id                              // _id in MongoDB
@Indexed                         // Index in MongoDB
@Field("email")                  // Field name in DB
```

---

## 🗄️ ORM Explained (MongoDB in Your Project)

### What is ORM?
**ORM = Object-Relational Mapping** (or Object-Document Mapping for MongoDB)

It maps your Java objects to database documents.

### In Mongoose (Express):
```javascript
const userSchema = new mongoose.Schema({
  email: String,
  name: String
});

const User = mongoose.model('User', userSchema);

// Usage
const user = new User({ email: 'test@example.com' });
await user.save();
```

### In Spring Data MongoDB:
```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String name;
    // Getters/Setters
}

// Repository (auto-implemented!)
interface UserRepository extends MongoRepository<User, String> {}

// Usage
User user = new User();
user.setEmail("test@example.com");
userRepository.save(user);
```

### Key Differences:

| Feature | Mongoose | Spring Data MongoDB |
|---------|----------|---------------------|
| **Schema** | Explicit schema | Java class = schema |
| **Model** | mongoose.model() | Not needed |
| **Queries** | Write methods | Just method names! |
| **CRUD** | Manual methods | Auto-generated |
| **Relations** | populate() | @DBRef or embedded |

### Magic of Spring Data:

**You write:**
```java
Optional<User> findByEmail(String email);
```

**Spring automatically generates:**
```javascript
// Equivalent to Mongoose:
User.findOne({ email: email });
```

**More examples:**
```java
List<User> findByFirstName(String name);        // find({ firstName: name })
User findByEmailAndPassword(String e, String p); // find({ email: e, password: p })
List<User> findByAgeGreaterThan(int age);       // find({ age: { $gt: age } })
```

Spring reads method names and generates queries! 🤯

---

## 🏗️ Architecture Comparison

### Express (Typical Structure):
```
src/
├── routes/
│   └── userRoutes.js       # Routes
├── controllers/
│   └── userController.js   # Logic
├── models/
│   └── User.js             # Mongoose model
├── middleware/
│   └── auth.js             # Middleware
└── app.js                  # Entry point
```

### Spring Boot (Your Project):
```
src/main/java/com/jobbot/
├── controller/
│   └── UserController.java     # Routes + Controller combined
├── service/
│   └── UserService.java        # Business logic
├── repository/
│   └── UserRepository.java     # Database access
├── model/
│   └── User.java               # Entity/Model
├── config/
│   └── SecurityConfig.java     # Configuration
└── JobBotApplication.java      # Entry point
```

**Pattern:**
- Express: Routes → Controller → Model
- Spring Boot: Controller → Service → Repository → Model

---

## 🔄 Dependency Injection (DI)

### Express (Manual):
```javascript
// userService.js
const UserRepository = require('./userRepository');
const userRepo = new UserRepository();

class UserService {
  constructor() {
    this.userRepo = userRepo;  // Manual
  }
}
```

### Spring Boot (Automatic):
```java
@Service
public class UserService {
    
    @Autowired  // Automatic injection!
    private UserRepository userRepository;
    
    // No constructor needed, Spring injects automatically!
}
```

**Magic:**
- Express: You manually `require()` and create instances
- Spring Boot: `@Autowired` automatically injects!

---

## 🚀 Running the Application

### Express:
```bash
node index.js
# or
npm start
# or
nodemon index.js  # Auto-reload
```

### Spring Boot:
```bash
mvn spring-boot:run
# or
./mvnw spring-boot:run  # Maven wrapper
# or
java -jar myapp.jar
```

**Hot Reload:**
- Express: `nodemon`
- Spring Boot: DevTools (already included!)

---

## 🧪 Testing

### Express (Jest):
```javascript
describe('User API', () => {
  it('should create user', async () => {
    const res = await request(app)
      .post('/api/users')
      .send({ email: 'test@example.com' });
    expect(res.status).toBe(200);
  });
});
```

### Spring Boot (JUnit):
```java
@SpringBootTest
class UserControllerTest {
    
    @Test
    void shouldCreateUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        
        // When
        ResponseEntity<User> response = userController.createUser(user);
        
        // Then
        assertEquals(200, response.getStatusCodeValue());
    }
}
```

---

## 📊 Quick Reference Table

| Express | Spring Boot | Purpose |
|---------|-------------|---------|
| `app.get()` | `@GetMapping` | GET endpoint |
| `app.post()` | `@PostMapping` | POST endpoint |
| `req.body` | `@RequestBody` | Request body |
| `req.params.id` | `@PathVariable` | URL parameter |
| `req.query.name` | `@RequestParam` | Query parameter |
| `res.json()` | `ResponseEntity.ok()` | JSON response |
| `res.status(404)` | `ResponseEntity.notFound()` | HTTP status |
| Middleware | Filters/Interceptors | Request processing |
| `mongoose.model()` | `@Document` class | Database model |
| `Model.find()` | `repository.findAll()` | Query database |
| `async/await` | Not needed! | Async handling |
| `.env` file | `application.yml` | Configuration |
| `npm install` | `mvn install` | Install dependencies |
| `package.json` | `pom.xml` | Dependencies file |

---

## 🎯 Key Takeaways for Express Developers:

1. **Annotations = Magic:**
   - Express: You write everything
   - Spring Boot: Annotations do the work

2. **Type Safety:**
   - Express: JavaScript (dynamic)
   - Spring Boot: Java (static typing)

3. **Auto-Configuration:**
   - Express: Manual setup
   - Spring Boot: Auto-configures based on dependencies

4. **Dependency Injection:**
   - Express: Manual `require()`
   - Spring Boot: Automatic `@Autowired`

5. **Repository Pattern:**
   - Express: Write all DB methods
   - Spring Boot: Just interface, Spring implements!

6. **Learning Curve:**
   - Express: Simpler, more code
   - Spring Boot: More concepts, less code

---

## 🚀 Next Steps:

1. **Start with Controllers:** Like Express routes
2. **Understand Annotations:** They're like decorators in TypeScript
3. **Learn Dependency Injection:** Most important concept
4. **Practice CRUD:** Similar to Mongoose operations
5. **Read Your Code:** See how it maps to Express

---

**Remember:** Spring Boot does a lot of "magic" behind the scenes. It's not complex, just highly automated! 🎩✨

