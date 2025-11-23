# API Documentation Guide

## 🚀 Automatic API Documentation (Swagger/OpenAPI)

This project uses **SpringDoc OpenAPI** (similar to FastAPI's automatic docs) to generate interactive API documentation.

---

## 📖 Access API Documentation

### After starting the application:

1. **Swagger UI** (Interactive Documentation):
   ```
   http://localhost:8080/swagger-ui.html
   ```
   or
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

2. **OpenAPI JSON** (Machine-readable):
   ```
   http://localhost:8080/v3/api-docs
   ```

3. **OpenAPI YAML**:
   ```
   http://localhost:8080/v3/api-docs.yaml
   ```

---

## ✨ Features

### Swagger UI Provides:
- ✅ **Interactive API Testing** - Test endpoints directly from browser
- ✅ **Request/Response Examples** - See example payloads
- ✅ **Schema Documentation** - View data models
- ✅ **Try It Out** - Execute API calls with real data
- ✅ **Authentication Support** - Test authenticated endpoints
- ✅ **Auto-generated** - Updates automatically when you add new endpoints

---

## 📝 Current API Endpoints

### Health Endpoints
- `GET /api/health` - Health check endpoint

### Admin Endpoints
- `POST /api/admin/seed` - Seed database with sample data

---

## 🎨 Customizing Documentation

### Add Tags to Controllers:
```java
@Tag(name = "Users", description = "User management endpoints")
@RestController
public class UserController {
    // ...
}
```

### Add Operation Details:
```java
@Operation(
    summary = "Get User",
    description = "Retrieves user information by ID"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User found"),
    @ApiResponse(responseCode = "404", description = "User not found")
})
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable String id) {
    // ...
}
```

### Add Schema Documentation:
```java
@Schema(description = "User information")
public class User {
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;
    
    // ...
}
```

---

## 🔧 Configuration

Documentation is configured in `OpenAPIConfig.java`:
- API Title & Description
- Version Information
- Server URLs
- Contact Information
- License Details

---

## 📚 Example: Adding New Endpoint

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    @GetMapping("/{id}")
    @Operation(
        summary = "Get User by ID",
        description = "Retrieves a specific user by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<User> getUser(@PathVariable String id) {
        // Implementation
    }
}
```

This will automatically appear in Swagger UI! 🎉

---

## 🎯 Benefits (Like FastAPI)

| Feature | FastAPI | Spring Boot + SpringDoc |
|---------|---------|------------------------|
| Auto Documentation | ✅ | ✅ |
| Interactive UI | ✅ | ✅ |
| OpenAPI Schema | ✅ | ✅ |
| Try It Out | ✅ | ✅ |
| Schema Validation | ✅ | ✅ |

---

## 🚀 Quick Start

1. **Start Application:**
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
   $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"
   .\mvnw.cmd spring-boot:run
   ```

2. **Open Browser:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Explore APIs:**
   - Click on any endpoint
   - Click "Try it out"
   - Fill in parameters
   - Click "Execute"
   - See response!

---

## 📸 What You'll See

- **Swagger UI** with all your endpoints
- **Grouped by tags** (Health, Admin, Users, etc.)
- **Expandable sections** for each endpoint
- **Request/Response schemas**
- **Example values**
- **Try it out** button for testing

---

## 🔒 Security (Future)

For authenticated endpoints, you can add:
```java
@SecurityRequirement(name = "bearerAuth")
```

And configure security schemes in `OpenAPIConfig.java`.

---

**Enjoy your automatic API documentation! 🎉**

