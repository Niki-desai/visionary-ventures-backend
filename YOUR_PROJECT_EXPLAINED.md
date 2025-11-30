# Your Project Explained - Practical Examples

## 🎯 Understanding Your Codebase

This guide explains YOUR actual code with Express comparisons.

---

## 📁 Your Project Structure

```
src/main/java/com/jobbot/
├── JobBotApplication.java          # Entry point (like app.js)
├── controller/                     # Routes/Controllers
│   ├── AuthController.java         # /api/auth/* routes
│   ├── AdminController.java        # /api/admin/* routes
│   └── HealthController.java       # /api/health route
├── service/                        # Business logic
│   ├── AuthService.java            # Auth logic
│   ├── JWTService.java             # JWT handling
│   ├── OTPService.java             # OTP generation
│   ├── EmailService.java           # Email sending
│   └── HealthService.java          # Health checks
├── repository/                     # Database access
│   ├── UserRepository.java
│   ├── JobRepository.java
│   ├── ApplicationRepository.java
│   └── ... (more repositories)
├── model/                          # Database models
│   ├── User.java
│   ├── Job.java
│   ├── Application.java
│   └── ... (more models)
├── dto/                            # Request/Response objects
│   ├── AuthRequest.java
│   ├── RegisterRequest.java
│   └── ... (more DTOs)
├── config/                         # Configuration
│   ├── SecurityConfig.java
│   ├── OpenAPIConfig.java
│   └── MongoConfig.java
└── exception/                      # Error handling
    └── GlobalExceptionHandler.java
```

---

## 🔍 Code Walkthrough: Authentication Flow

### Example 1: Login Endpoint

**Express version:**
```javascript
// routes/auth.js
router.post('/login', async (req, res) => {
    const { email, password } = req.body;
    
    // Find user
    const user = await User.findOne({ email });
    if (!user) {
        return res.status(401).json({ error: 'Invalid credentials' });
    }
    
    // Check password
    const isValid = await bcrypt.compare(password, user.password);
    if (!isValid) {
        return res.status(401).json({ error: 'Invalid credentials' });
    }
    
    // Generate token
    const token = jwt.sign({ userId: user._id }, SECRET);
    
    res.json({ token, user });
});
```

**Your Spring Boot version:**

**AuthController.java:**
```java
@PostMapping("/login")
@Operation(summary = "Login")
public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
    ApiResponse response = authService.login(request);
    return ResponseEntity.ok(response);
}
```

**AuthService.java:**
```java
public ApiResponse login(AuthRequest request) {
    // Find user
    Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
    if (userOpt.isEmpty()) {
        return ApiResponse.error("Invalid email or password");
    }
    
    User user = userOpt.get();
    
    // Check password
    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
        return ApiResponse.error("Invalid email or password");
    }
    
    // Generate token
    String token = jwtService.generateToken(user.getId(), user.getEmail());
    AuthResponse authResponse = new AuthResponse(token, user.getId(), 
            user.getEmail(), user.getFirstName(), user.getLastName());    
    return ApiResponse.success("Login successful", authResponse);
}
```

**Key points:**
- Controller is thin (just calls service)
- Service has business logic
- Repository handles DB (auto-implemented!)
- DTOs for request/response

---

## 🔐 Example 2: Registration with OTP

### Flow:
```
1. POST /api/auth/register → Send OTP
2. POST /api/auth/verify-otp → Verify OTP
3. Return JWT token
```

### Your Code:

**Step 1: Register**
```java
@PostMapping("/register")
public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
    ApiResponse response = authService.register(request);
    return ResponseEntity.ok(response);
}
```

**AuthService.register():**
```java
public ApiResponse register(RegisterRequest request) {
    // 1. Check if email exists
    if (userRepository.existsByEmail(request.getEmail())) {
        return ApiResponse.error("Email already registered");
    }
    
    // 2. Create user
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setIsActive(false);  // Not active until OTP verified
    
    // 3. Generate OTP
    String otp = otpService.generateOTP();
    otpService.storeOTP(request.getEmail(), otp, OTPService.OTPType.REGISTRATION);
    
    // 4. Send email
    emailService.sendRegistrationOTP(request.getEmail(), otp);
    
    // 5. Save user
    userRepository.save(user);
    
    return ApiResponse.success("OTP sent to email");
}
```

**Express equivalent:**
```javascript
router.post('/register', async (req, res) => {
    const { email, password } = req.body;
    
    // Check exists
    const exists = await User.findOne({ email });
    if (exists) return res.status(400).json({ error: 'Email exists' });
    
    // Create user
    const user = new User({
        email,
        password: await bcrypt.hash(password, 10),
        isActive: false
    });
    
    // Generate OTP
    const otp = generateOTP();
    await OTP.create({ email, otp, expiresAt: Date.now() + 600000 });
    
    // Send email
    await sendEmail(email, otp);
    
    // Save user
    await user.save();
    
    res.json({ message: 'OTP sent' });
});
```

**Same logic, different syntax!**

---

## 🗄️ Example 3: Database Queries

### Your UserRepository:
```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

**What Spring generates:**

**findByEmail:**
```javascript
// Mongoose equivalent:
User.findOne({ email: email });
```

**existsByEmail:**
```javascript
// Mongoose equivalent:
const exists = await User.exists({ email: email });
```

**Built-in methods:**
```java
userRepository.findAll();        // User.find()
userRepository.findById(id);     // User.findById(id)
userRepository.save(user);       // user.save() or User.create()
userRepository.deleteById(id);   // User.findByIdAndDelete(id)
userRepository.count();          // User.countDocuments()
```

---

## 🔧 Example 4: Configuration Files

### Your application.yml:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/jobbot

server:
  port: 8080

jwt:
  secret: your-secret-key
  expiration: 86400000
```

**Express equivalent (.env):**
```bash
MONGODB_URI=mongodb://localhost:27017/jobbot
PORT=8080
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

**Usage in Java:**
```java
@Value("${jwt.secret}")
private String jwtSecret;
```

**Express equivalent:**
```javascript
const jwtSecret = process.env.JWT_SECRET;
```

---

## 🎨 Example 5: Your Models

### Your User.java:
```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    @Field("email")
    private String email;
    
    @Field("password_hash")
    private String passwordHash;
    
    @Field("first_name")
    private String firstName;
    
    // ... more fields
    
    // Getters and Setters
}
```

**Express/Mongoose equivalent:**
```javascript
const userSchema = new mongoose.Schema({
    email: { type: String, unique: true, required: true },
    passwordHash: String,
    firstName: String,
    // ... more fields
    createdAt: { type: Date, default: Date.now }
});

const User = mongoose.model('User', userSchema);
```

**Key differences:**
- Java: Class with annotations
- Mongoose: Schema object
- Java: Explicit getters/setters
- Mongoose: Direct property access

---

## 🔐 Example 6: JWT Service

### Your JWTService.java:
```java
@Service
public class JWTService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    public String generateToken(String userId, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }
}
```

**Express equivalent:**
```javascript
const jwt = require('jsonwebtoken');

function generateToken(userId, email) {
    return jwt.sign(
        { userId, email },
        process.env.JWT_SECRET,
        { expiresIn: '24h' }
    );
}

function extractUserId(token) {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    return decoded.userId;
}
```

**Same functionality, different libraries!**

---

## 🎯 Practical Tips

### 1. **Read your AuthController.java**
See how endpoints are defined. It's like Express routes!

### 2. **Check AuthService.java**
Business logic is here. Like your Express service files.

### 3. **Look at Repositories**
Just interfaces! Spring implements everything.

### 4. **Try Swagger UI**
```
http://localhost:8080/swagger-ui.html
```
Test APIs without Postman!

### 5. **Use DevTools**
Changes auto-reload like nodemon.

---

## 📊 Comparison Summary

### Express Strengths:
- ✅ Simpler to learn
- ✅ Faster prototyping
- ✅ JavaScript ecosystem
- ✅ Less boilerplate

### Spring Boot Strengths:
- ✅ Type safety
- ✅ Auto-configuration
- ✅ Enterprise features
- ✅ Better for large projects
- ✅ Built-in security
- ✅ Amazing tooling

---

## 💡 Final Tips

1. **Don't memorize annotations** - Understand what they do
2. **Think in layers** - Controller → Service → Repository
3. **Use your IDE** - Auto-completion helps a lot
4. **Read error messages** - They're detailed in Java
5. **Test in Swagger** - Interactive testing is faster

---

## 🚀 Quick Command Reference

```bash
# Start app (like npm start)
.\mvnw.cmd spring-boot:run

# Install dependencies (like npm install)
.\mvnw.cmd clean install

# Run tests (like npm test)
.\mvnw.cmd test

# View docs
http://localhost:8080/swagger-ui.html

# Check health
http://localhost:8080/api/health
```

---

**You're an Express dev, you'll pick this up quickly! Same concepts, different syntax! 🎯**

