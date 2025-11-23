# Visionary Ventures Backend

**AI Job Finder & Auto-Application Assistant Backend**

A Spring Boot-based backend service that leverages AI (OpenAI/Local Models) to help users find jobs and automatically apply to them using an agentic workflow.

## System Architecture

- **Backend**: Spring Boot (Java 17)
- **Database**: MongoDB
- **AI Layer**: LLM-powered Agentic Workflow (OpenAI / Local Model)

## Project Structure

```
src/main/java/com/jobbot
    ├── controller
    │       └── HealthController.java
    ├── service
    │       └── HealthService.java
    ├── repository
    ├── model
    ├── config
    │       ├── OpenAIConfig.java
    │       └── MongoConfig.java
    ├── exception
    │       └── GlobalExceptionHandler.java
    └── JobBotApplication.java

src/main/resources
    ├── application.yml
```

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MongoDB** (local or remote instance)
- **Git**

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Niki-desai/visionary-ventures-backend.git
cd visionary-ventures-backend
```

### 2. Configure Environment Variables

Create a `.env` file in the root directory (or set environment variables):

```bash
# MongoDB Configuration
MONGODB_URI=mongodb://localhost:27017/jobbot
MONGODB_DATABASE=jobbot

# OpenAI Configuration
OPENAI_API_KEY=your-openai-api-key-here
OPENAI_MODEL=gpt-4
OPENAI_TEMPERATURE=0.7
OPENAI_MAX_TOKENS=2000

# Server Configuration
SERVER_PORT=8081

# Local AI Configuration (Optional)
LOCAL_AI_ENABLED=false
LOCAL_AI_ENDPOINT=http://localhost:11434
LOCAL_AI_MODEL=llama2
```

### 3. Install Dependencies

```bash
mvn clean install
```

### 4. Start MongoDB

**Option A: Local MongoDB**
```bash
# Windows
mongod

# Linux/Mac
sudo systemctl start mongod
# or
mongod --dbpath /path/to/data
```

**Option B: MongoDB Atlas (Cloud)**
- Create a free cluster at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
- Get your connection string and update `MONGODB_URI` in your environment

### 5. Run the Application

**Option A: Using Maven**
```bash
mvn spring-boot:run
```

**Option B: Using Java**
```bash
mvn clean package
java -jar target/visionary-ventures-backend-1.0.0.jar
```

**Option C: Using IDE**
- Import the project in IntelliJ IDEA or Eclipse
- Run `JobBotApplication.java` as a Spring Boot application

### 6. Verify the Setup

Once the application is running, test the health endpoint:

```bash
curl http://localhost:8080/api/health
```

Or open in browser: `http://localhost:8080/api/health`

Expected response:
```json
{
  "status": "UP",
  "timestamp": "2024-01-01T12:00:00",
  "service": "Visionary Ventures Backend",
  "version": "1.0.0"
}
```

## Development Commands

### Build the Project
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Run with Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Check Dependencies
```bash
mvn dependency:tree
```

### Clean Build
```bash
mvn clean install
```

## API Endpoints

### Health Check
- **GET** `/api/health` - Check application health status

## Architecture Flow

```
Controller → Service → Repository → MongoDB
                ↓
            AI Service (OpenAI/Local)
```

## Dependencies

### Core Dependencies
- **Spring Boot Web** - REST API framework
- **Spring Data MongoDB** - MongoDB integration
- **OpenAI Java Client** - AI/LLM integration
- **Lombok** - Reduces boilerplate code
- **Spring Boot Validation** - Input validation

## Configuration

All configuration is managed through `application.yml`. Key configurations:

- **MongoDB**: Connection string and database name
- **OpenAI**: API key, model, temperature, max tokens
- **Server**: Port and context path
- **Logging**: Log levels and patterns

## Development Guidelines

1. **Repository Layer**: Handle all database operations
2. **Service Layer**: Business logic and AI integration
3. **Controller Layer**: REST API endpoints
4. **Model Layer**: Domain entities and DTOs
5. **Config Layer**: Bean configurations and settings
6. **Exception Layer**: Global exception handling

## Troubleshooting

### MongoDB Connection Issues
- Ensure MongoDB is running: `mongosh` or `mongo`
- Check connection string in `application.yml`
- Verify network/firewall settings

### OpenAI API Issues
- Verify API key is correct
- Check API quota and billing
- Ensure model name is valid

### Port Already in Use
- Change `SERVER_PORT` in `application.yml`
- Or kill the process using port 8080

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

[Add your license here]

## Contact

[Add contact information here]


for docs -> $env:JAVA_HOME = "C:\Program Files\Java\jdk-25"; $env:PATH = "C:\Program Files\Java\jdk-25\bin;$env:PATH"; .\mvnw.cmd spring-boot:run
