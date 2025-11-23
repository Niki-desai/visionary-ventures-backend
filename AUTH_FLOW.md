# Authentication Flow Documentation

## 🔐 Complete Authentication System

### Features Implemented:
- ✅ Email/Password Registration with OTP verification
- ✅ Email/Password Login
- ✅ OAuth Login/Registration (Google, GitHub, LinkedIn, etc.)
- ✅ Forgot Password with OTP
- ✅ Password Reset
- ✅ JWT Token Authentication
- ✅ Email OTP Service

---

## 📋 API Endpoints

### 1. Register (Step 1)
**POST** `/api/auth/register`

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Registration successful. OTP sent to your email. Please verify to complete registration."
}
```

---

### 2. Verify Registration OTP (Step 2)
**POST** `/api/auth/verify-otp`

**Request:**
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Registration verified successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "message": "Registration completed successfully"
  }
}
```

---

### 3. Login
**POST** `/api/auth/login`

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "507f1f77bcf86cd799439011",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "message": "Login successful"
  }
}
```

---

### 4. OAuth Login/Register
**POST** `/api/auth/oauth/{provider}`

**Path Parameters:**
- `provider`: `google`, `github`, `linkedin`, etc.

**Request:**
```json
{
  "oauthId": "123456789",
  "email": "user@gmail.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "507f1f77bcf86cd799439011",
    "email": "user@gmail.com",
    "firstName": "John",
    "lastName": "Doe",
    "message": "Login successful"
  }
}
```

---

### 5. Forgot Password (Step 1 - Send OTP)
**POST** `/api/auth/forgot-password`

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "OTP sent to your email. Please check and verify."
}
```

---

### 6. Verify Forgot Password OTP (Step 2)
**POST** `/api/auth/verify-forgot-password-otp`

**Request:**
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "OTP verified successfully. You can now reset your password."
}
```

---

### 7. Reset Password (Step 3)
**POST** `/api/auth/reset-password`

**Request:**
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "newPassword": "newpassword123"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Password reset successfully. You can now login with your new password."
}
```

---

## 🔄 Flow Diagrams

### Registration Flow:
```
1. POST /api/auth/register
   ↓
2. User created (inactive)
   ↓
3. OTP sent to email
   ↓
4. POST /api/auth/verify-otp
   ↓
5. User activated + JWT token returned
```

### Login Flow:
```
1. POST /api/auth/login
   ↓
2. Validate credentials
   ↓
3. Generate JWT token
   ↓
4. Return token + user info
```

### OAuth Flow:
```
1. User authenticates with OAuth provider
   ↓
2. Frontend receives OAuth data
   ↓
3. POST /api/auth/oauth/{provider}
   ↓
4. Create/Update user + Generate JWT token
   ↓
5. Return token + user info
```

### Forgot Password Flow:
```
1. POST /api/auth/forgot-password
   ↓
2. OTP sent to email
   ↓
3. POST /api/auth/verify-forgot-password-otp
   ↓
4. OTP verified
   ↓
5. POST /api/auth/reset-password
   ↓
6. Password updated
```

---

## 🔧 Configuration

### JWT Configuration
In `application.yml`:
```yaml
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key}
  expiration: ${JWT_EXPIRATION:86400000} # 24 hours
```

### Email Configuration
In `application.yml`:
```yaml
spring:
  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME:your-email@gmail.com}
    password: ${MAIL_PASSWORD:your-app-password}
```

### Gmail Setup:
1. Enable 2-Step Verification
2. Generate App Password: https://myaccount.google.com/apppasswords
3. Use App Password in `MAIL_PASSWORD`

---

## 🔒 Security Features

- ✅ Password hashing with BCrypt
- ✅ JWT token authentication
- ✅ OTP expiration (10 minutes)
- ✅ Email verification
- ✅ Account activation required
- ✅ CSRF disabled (for API)
- ✅ Stateless sessions

---

## 📝 Using JWT Token

After login/registration, include token in requests:

```
Authorization: Bearer <your-jwt-token>
```

Example:
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
     http://localhost:8080/api/protected-endpoint
```

---

## 🧪 Testing with Swagger

1. Start application
2. Open: `http://localhost:8080/swagger-ui.html`
3. Navigate to "Authentication" section
4. Try endpoints:
   - Register → Verify OTP → Login
   - Forgot Password → Verify OTP → Reset Password
   - OAuth Login

---

## 📦 Dependencies Added

- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-oauth2-client` - OAuth support
- `spring-boot-starter-mail` - Email service
- `jjwt` - JWT token handling

---

## 🚀 Next Steps

1. **Add JWT Filter** - For protected endpoints
2. **Add Refresh Token** - For token renewal
3. **Add Rate Limiting** - For OTP requests
4. **Add Redis** - For OTP storage (production)
5. **Add OAuth2 Client** - For direct OAuth integration

---

**All authentication flows are now implemented! 🎉**

