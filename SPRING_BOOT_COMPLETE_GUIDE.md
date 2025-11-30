# Spring Boot Complete Guide - All Topics

## 📚 Table of Contents

1. [Introduction](#introduction)
2. [Inversion of Control & Dependency Injection](#inversion-of-control--dependency-injection)
3. [REST API Development](#rest-api-development)
4. [Advanced Spring Boot Features](#advanced-spring-boot-features)
5. [Background & Async Tasks](#background--async-tasks)
6. [Microservices with Spring Boot](#microservices-with-spring-boot)
7. [Spring Boot with Kafka](#spring-boot-with-kafka)

---

## Introduction

### Spring vs Spring Boot

**Spring Framework:**
- Core framework for Java applications
- Requires manual configuration
- XML-based or annotation-based configuration
- You choose and configure everything manually
- More control, more setup

**Spring Boot:**
- Built on top of Spring Framework
- Auto-configuration (convention over configuration)
- Opinionated defaults
- Embedded server (Tomcat, Jetty, Undertow)
- Production-ready features out of the box
- Less configuration, faster development

**Key Differences:**

| Feature | Spring | Spring Boot |
|---------|--------|-------------|
| **Configuration** | Manual (XML/Java) | Auto-configuration |
| **Server** | External (WAR file) | Embedded |
| **Dependencies** | Manual management | Starter dependencies |
| **Setup Time** | Longer | Shorter |
| **Learning Curve** | Steeper | Easier |
| **Use Case** | Complex, custom needs | Rapid development |

**Example:**

**Spring (Traditional):**
```java
// web.xml
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-config.xml</param-value>
    </init-param>
</servlet>
```

**Spring Boot:**
```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
// That's it! Auto-configured!
```

---

### Spring MVC vs Spring Boot

**Spring MVC:**
- Part of Spring Framework
- Web framework for building web applications
- Requires Spring configuration
- Manual setup of DispatcherServlet, ViewResolver, etc.

**Spring Boot:**
- Includes Spring MVC
- Auto-configures Spring MVC
- Adds embedded server, auto-configuration
- Production-ready features

**Relationship:**
```
Spring Boot = Spring Framework + Spring MVC + Auto-configuration + Embedded Server
```

---

### Create Your First Spring Boot Project

#### Option 1: Spring Initializr (Recommended)

**Website:** https://start.spring.io/

1. Select:
   - **Project:** Maven
   - **Language:** Java
   - **Spring Boot:** Latest version
   - **Packaging:** Jar
   - **Java:** 17 or 21

2. Add Dependencies:
   - Spring Web
   - Spring Data MongoDB (if using MongoDB)
   - Lombok (optional)

3. Generate and Download

4. Extract and open in IDE

#### Option 2: IntelliJ IDEA

1. **File → New → Project**
2. Select **Spring Initializr**
3. Choose:
   - SDK: Java 17+
   - Spring Boot version
   - Project metadata
4. Select dependencies
5. Click **Next** → **Finish**

#### Option 3: Eclipse/STS

1. **File → New → Spring Starter Project**
2. Configure project
3. Select dependencies
4. Click **Finish**

#### Option 4: VS Code

1. Install **Spring Boot Extension Pack**
2. **Command Palette** → "Spring Initializr: Generate a Maven Project"
3. Follow prompts

---

### Run Spring Boot Application

#### Method 1: IDE
- Right-click `Application.java` → **Run**

#### Method 2: Maven
```bash
mvn spring-boot:run
```

#### Method 3: Gradle
```bash
./gradlew bootRun
```

#### Method 4: JAR File
```bash
mvn clean package
java -jar target/myapp.jar
```

#### Method 5: Maven Wrapper
```bash
./mvnw spring-boot:run
```

---

## Inversion of Control & Dependency Injection

### Inversion of Control (IoC)

**What is IoC?**
- Traditional: You create and manage objects
- IoC: Framework creates and manages objects
- Control is "inverted" to the framework

**Example:**

**Without IoC:**
```java
public class UserService {
    private UserRepository userRepository;
    
    public UserService() {
        this.userRepository = new UserRepository(); // You create
    }
}
```

**With IoC:**
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; // Spring creates
}
```

---

### Dependency Injection (DI)

**What is DI?**
- Providing dependencies to objects
- Instead of creating dependencies yourself, they're "injected"

**Types of DI:**

#### 1. Constructor Injection (Recommended)
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired // Optional in Spring 4.3+
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**Benefits:**
- Required dependencies (can't be null)
- Immutable (final fields)
- Easy to test

#### 2. Field Injection
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

**Drawbacks:**
- Hard to test (need reflection)
- Can't make field final
- Hidden dependencies

#### 3. Setter Injection
```java
@Service
public class UserService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

---

### BeanFactory vs ApplicationContext

**BeanFactory:**
- Basic IoC container
- Lazy loading (beans created on demand)
- Lightweight
- Less features

**ApplicationContext:**
- Advanced IoC container
- Eager loading (beans created at startup)
- More features:
  - Internationalization
  - Event publishing
  - Application-layer specific contexts
  - AOP integration

**Spring Boot uses ApplicationContext by default.**

```java
// BeanFactory
BeanFactory factory = new XmlBeanFactory(new FileSystemResource("beans.xml"));
UserService service = factory.getBean(UserService.class);

// ApplicationContext
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
UserService service = context.getBean(UserService.class);
```

---

### Spring Bean Lifecycle

**Bean Lifecycle Stages:**

1. **Instantiation** - Bean object created
2. **Populate Properties** - Dependencies injected
3. **BeanNameAware** - Bean name set
4. **BeanFactoryAware** - BeanFactory reference set
5. **ApplicationContextAware** - ApplicationContext reference set
6. **Pre-initialization** - BeanPostProcessors
7. **@PostConstruct** - Custom initialization
8. **InitializingBean** - afterPropertiesSet()
9. **Post-initialization** - BeanPostProcessors
10. **Bean Ready** - Available for use
11. **@PreDestroy** - Before destruction
12. **DisposableBean** - destroy()
13. **Bean Destroyed**

**Example:**
```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    
    @PostConstruct
    public void init() {
        System.out.println("PostConstruct called");
    }
    
    @Override
    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet called");
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("PreDestroy called");
    }
    
    @Override
    public void destroy() {
        System.out.println("destroy called");
    }
}
```

**Order:**
1. Constructor
2. @PostConstruct
3. afterPropertiesSet()
4. ... (bean ready)
5. @PreDestroy
6. destroy()

---

### Singleton vs Prototype Scope

**Singleton (Default):**
- One instance per Spring container
- Shared across all requests
- Thread-safe (if stateless)

```java
@Component
@Scope("singleton") // Default
public class UserService {
    // One instance for entire application
}
```

**Prototype:**
- New instance every time
- Not managed by Spring after creation
- Use for stateful beans

```java
@Component
@Scope("prototype")
public class UserService {
    // New instance each time
}
```

**Other Scopes:**
- **Request** - One per HTTP request
- **Session** - One per HTTP session
- **Application** - One per ServletContext
- **WebSocket** - One per WebSocket session

---

### Custom Scope

```java
@Component
@Scope("custom")
public class CustomScopedBean {
    // Custom scope logic
}

// Register custom scope
@Configuration
public class ScopeConfig {
    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("custom", new CustomScope());
        return configurer;
    }
}
```

---

### Create a Spring Bean

#### Method 1: @Component (and stereotypes)
```java
@Component
public class UserService {
    // Auto-detected by component scanning
}
```

**Stereotypes:**
- `@Component` - Generic component
- `@Service` - Business logic
- `@Repository` - Data access
- `@Controller` - Web controller
- `@RestController` - REST API

#### Method 2: @Bean (in @Configuration)
```java
@Configuration
public class AppConfig {
    
    @Bean
    public UserService userService() {
        return new UserService();
    }
}
```

#### Method 3: @Import
```java
@Configuration
@Import({UserService.class})
public class AppConfig {
}
```

---

### Spring Autowiring

**Autowiring Modes:**

#### 1. byType (Default)
```java
@Autowired
private UserRepository userRepository; // Matches by type
```

#### 2. byName
```java
@Autowired
@Qualifier("userRepository")
private UserRepository repository; // Matches by name
```

#### 3. Constructor
```java
@Autowired
public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

#### 4. @Primary
```java
@Primary
@Repository
public class UserRepositoryImpl implements UserRepository {
    // This will be injected by default
}
```

#### 5. @Qualifier
```java
@Autowired
@Qualifier("userRepositoryImpl")
private UserRepository userRepository;
```

---

### DispatcherServlet

**What is DispatcherServlet?**
- Front controller in Spring MVC
- Handles all HTTP requests
- Routes to appropriate controllers

**How it works:**
```
HTTP Request
    ↓
DispatcherServlet
    ↓
HandlerMapping (finds controller)
    ↓
Controller (processes request)
    ↓
ModelAndView
    ↓
ViewResolver (finds view)
    ↓
View (renders response)
    ↓
HTTP Response
```

**In Spring Boot:**
- Auto-configured
- No XML configuration needed
- Customizable via properties

```yaml
spring:
  mvc:
    servlet:
      path: /api/* # Custom path
```

---

### Spring IoC Container

**What is IoC Container?**
- Manages beans (objects)
- Creates, configures, wires dependencies
- Manages lifecycle

**Types:**
1. **BeanFactory** - Basic container
2. **ApplicationContext** - Advanced container (used by Spring Boot)

**Container Responsibilities:**
- Bean instantiation
- Dependency injection
- Lifecycle management
- Configuration management

---

### Maven/Gradle (Project Build Tools)

#### Maven

**pom.xml Structure:**
```xml
<project>
    <groupId>com.example</groupId>
    <artifactId>myapp</artifactId>
    <version>1.0.0</version>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
```

**Common Commands:**
```bash
mvn clean          # Clean target directory
mvn compile        # Compile code
mvn test           # Run tests
mvn package        # Create JAR/WAR
mvn install        # Install to local repository
mvn spring-boot:run # Run Spring Boot app
```

#### Gradle

**build.gradle:**
```gradle
plugins {
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```

**Common Commands:**
```bash
./gradlew clean      # Clean build directory
./gradlew build      # Build project
./gradlew test       # Run tests
./gradlew bootRun    # Run Spring Boot app
```

---

## REST API Development

### @RestController

**What it does:**
- Combines `@Controller` + `@ResponseBody`
- Returns data (JSON/XML) instead of views
- Auto-serializes objects to JSON

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    // All methods return JSON by default
}
```

**vs @Controller:**
```java
@Controller
public class UserController {
    @ResponseBody // Need this for JSON
    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }
}
```

---

### @RequestMapping

**Base path for controller:**
```java
@RestController
@RequestMapping("/api/users") // Base path
public class UserController {
    
    @GetMapping // /api/users
    public List<User> getUsers() { }
    
    @GetMapping("/{id}") // /api/users/{id}
    public User getUser(@PathVariable String id) { }
}
```

**Attributes:**
```java
@RequestMapping(
    value = "/users",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
```

---

### @GetMapping & @PostMapping

```java
@GetMapping("/users")
public List<User> getUsers() {
    return userService.findAll();
}

@PostMapping("/users")
public User createUser(@RequestBody User user) {
    return userService.save(user);
}
```

**Shortcuts:**
- `@GetMapping` = `@RequestMapping(method = RequestMethod.GET)`
- `@PostMapping` = `@RequestMapping(method = RequestMethod.POST)`
- `@PutMapping` = `@RequestMapping(method = RequestMethod.PUT)`
- `@DeleteMapping` = `@RequestMapping(method = RequestMethod.DELETE)`
- `@PatchMapping` = `@RequestMapping(method = RequestMethod.PATCH)`

---

### @PutMapping & @DeleteMapping

```java
@PutMapping("/users/{id}")
public User updateUser(@PathVariable String id, @RequestBody User user) {
    return userService.update(id, user);
}

@DeleteMapping("/users/{id}")
public void deleteUser(@PathVariable String id) {
    userService.delete(id);
}
```

---

### @PathVariable & @RequestParam

**@PathVariable:**
```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable String id) {
    // GET /users/123 → id = "123"
}

@GetMapping("/users/{userId}/posts/{postId}")
public Post getPost(
    @PathVariable String userId,
    @PathVariable String postId
) {
    // GET /users/123/posts/456
}
```

**@RequestParam:**
```java
@GetMapping("/users")
public List<User> searchUsers(
    @RequestParam String name,
    @RequestParam(required = false) Integer age,
    @RequestParam(defaultValue = "10") Integer limit
) {
    // GET /users?name=John&age=25&limit=20
}
```

**Optional Parameters:**
```java
@RequestParam(required = false) String name
@RequestParam(defaultValue = "0") int page
@RequestParam(value = "user_name") String userName // Different param name
```

---

### @RequestBody

**Convert JSON to Java object:**
```java
@PostMapping("/users")
public User createUser(@RequestBody User user) {
    // JSON → User object automatically
    return userService.save(user);
}
```

**Request:**
```json
POST /api/users
Content-Type: application/json

{
  "name": "John",
  "email": "john@example.com"
}
```

**Spring automatically:**
1. Deserializes JSON to User object
2. Validates (if @Valid)
3. Passes to method

---

### REST API Best Practices

**HTTP Methods:**
- `GET` - Retrieve data
- `POST` - Create resource
- `PUT` - Update (full update)
- `PATCH` - Partial update
- `DELETE` - Delete resource

**Status Codes:**
- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Success, no body
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - Not authorized
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

**Response Example:**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    User saved = userService.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}
```

---

### JSON Serialization/Deserialization

**Automatic with Jackson:**
- Spring Boot includes Jackson by default
- Auto-converts objects ↔ JSON

**Custom Serialization:**
```java
@JsonIgnore
private String password; // Exclude from JSON

@JsonProperty("user_name")
private String userName; // Different JSON name

@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate birthDate; // Custom date format
```

**Custom ObjectMapper:**
```java
@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        return mapper;
    }
}
```

---

### Exception Handling

#### Global Exception Handler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
        UserNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            "USER_NOT_FOUND",
            ex.getMessage()
        );
        return ResponseEntity.status(404).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An error occurred"
        );
        return ResponseEntity.status(500).body(error);
    }
}
```

#### Controller-Level Exception Handling
```java
@RestController
public class UserController {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
```

---

### Validation

**Add Dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Validation Annotations:**
```java
public class User {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email")
    @NotBlank
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100)
    private Integer age;
    
    @Size(min = 6, max = 20)
    private String password;
    
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;
}
```

**Use in Controller:**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(
    @Valid @RequestBody User user
) {
    // Validation happens automatically
    // If invalid, returns 400 Bad Request
    return ResponseEntity.ok(userService.save(user));
}
```

**Custom Validator:**
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {
    String message() default "Invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

---

## Advanced Spring Boot Features

### Scheduling Tasks

**Enable Scheduling:**
```java
@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**Fixed Rate:**
```java
@Scheduled(fixedRate = 5000) // Every 5 seconds
public void doSomething() {
    System.out.println("Task executed");
}
```

**Fixed Delay:**
```java
@Scheduled(fixedDelay = 5000) // 5 seconds after previous completes
public void doSomething() {
    // Task
}
```

**Cron Expression:**
```java
@Scheduled(cron = "0 0 12 * * ?") // Every day at noon
public void dailyTask() {
    // Task
}
```

**Cron Examples:**
- `0 0 12 * * ?` - Every day at 12:00 PM
- `0 0 0 * * ?` - Every day at midnight
- `0 0 0 1 * ?` - First day of every month
- `0 0 0 ? * MON` - Every Monday at midnight
- `0 */5 * * * ?` - Every 5 minutes

**Custom Scheduler:**
```java
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
    
    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}
```

---

### Sending Emails

**Add Dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

**Configuration:**
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

**Send Email:**
```java
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML
        
        mailSender.send(message);
    }
}
```

**With Attachments:**
```java
public void sendEmailWithAttachment(String to, String subject, String body, String filePath) {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body);
    
    FileSystemResource file = new FileSystemResource(new File(filePath));
    helper.addAttachment("document.pdf", file);
    
    mailSender.send(message);
}
```

---

### File Handling & Uploading Files

**Configuration:**
```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 10MB
```

**Upload File:**
```java
@PostMapping("/upload")
public ResponseEntity<String> uploadFile(
    @RequestParam("file") MultipartFile file
) {
    if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("File is empty");
    }
    
    try {
        // Save file
        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");
        Files.createDirectories(uploadPath);
        
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return ResponseEntity.ok("File uploaded: " + fileName);
    } catch (IOException e) {
        return ResponseEntity.status(500).body("Upload failed");
    }
}
```

**Download File:**
```java
@GetMapping("/download/{fileName}")
public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
    Path filePath = Paths.get("uploads").resolve(fileName);
    Resource resource = new UrlResource(filePath.toUri());
    
    if (resource.exists() && resource.isReadable()) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=\"" + fileName + "\"")
            .body(resource);
    } else {
        return ResponseEntity.notFound().build();
    }
}
```

**Multiple Files:**
```java
@PostMapping("/upload-multiple")
public ResponseEntity<String> uploadFiles(
    @RequestParam("files") MultipartFile[] files
) {
    // Process each file
    for (MultipartFile file : files) {
        // Save file
    }
    return ResponseEntity.ok("Files uploaded");
}
```

---

### Caching

**Enable Caching:**
```java
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**Use Cache:**
```java
@Service
public class UserService {
    
    @Cacheable("users") // Cache result
    public User getUserById(String id) {
        // Expensive operation
        return userRepository.findById(id);
    }
    
    @CacheEvict(value = "users", key = "#id") // Remove from cache
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    
    @CachePut(value = "users", key = "#user.id") // Update cache
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
```

**Cache Configuration:**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("users"),
            new ConcurrentMapCache("jobs")
        ));
        return cacheManager;
    }
}
```

---

### Caching with Other Providers

#### Redis Cache
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

```java
@Configuration
@EnableCaching
public class RedisCacheConfig {
    
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10));
        
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}
```

#### Caffeine Cache
```xml
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

```java
@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "jobs");
    cacheManager.setCaffeine(Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(1000));
    return cacheManager;
}
```

---

### Transaction Management

**Declarative Transactions:**
```java
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    public void transferMoney(String from, String to, double amount) {
        // All or nothing - transaction
        accountRepository.debit(from, amount);
        accountRepository.credit(to, amount);
    }
}
```

**Transaction Attributes:**
```java
@Transactional(
    propagation = Propagation.REQUIRED, // Default
    isolation = Isolation.READ_COMMITTED,
    timeout = 30,
    readOnly = false,
    rollbackFor = Exception.class
)
public void method() {
    // Transaction logic
}
```

**Propagation Types:**
- `REQUIRED` - Join existing or create new
- `REQUIRES_NEW` - Always create new
- `SUPPORTS` - Join if exists, otherwise no transaction
- `NOT_SUPPORTED` - Suspend current transaction
- `MANDATORY` - Must have transaction
- `NEVER` - Must not have transaction
- `NESTED` - Nested transaction

**Manual Transaction:**
```java
@Autowired
private PlatformTransactionManager transactionManager;

public void manualTransaction() {
    TransactionStatus status = transactionManager.getTransaction(
        new DefaultTransactionDefinition()
    );
    
    try {
        // Do work
        transactionManager.commit(status);
    } catch (Exception e) {
        transactionManager.rollback(status);
    }
}
```

---

### DTO Mapping

**Manual Mapping:**
```java
public UserDTO toDTO(User user) {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    return dto;
}
```

**MapStruct (Recommended):**
```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
```

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOList(List<User> users);
}
```

**Usage:**
```java
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    public UserDTO getUser(String id) {
        User user = userRepository.findById(id);
        return userMapper.toDTO(user);
    }
}
```

**ModelMapper:**
```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.1.1</version>
</dependency>
```

```java
@Bean
public ModelMapper modelMapper() {
    return new ModelMapper();
}

// Usage
UserDTO dto = modelMapper.map(user, UserDTO.class);
```

---

## Background & Async Tasks

### @Async Annotation

**Enable Async:**
```java
@SpringBootApplication
@EnableAsync
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**Async Method:**
```java
@Service
public class EmailService {
    
    @Async
    public CompletableFuture<String> sendEmailAsync(String to, String subject) {
        // This runs in background thread
        sendEmail(to, subject, "Body");
        return CompletableFuture.completedFuture("Email sent");
    }
}
```

**Custom Executor:**
```java
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}

// Use specific executor
@Async("taskExecutor")
public void method() {
    // Task
}
```

**Wait for Result:**
```java
@Async
public CompletableFuture<String> processData(String data) {
    // Process
    return CompletableFuture.completedFuture("Result");
}

// Usage
CompletableFuture<String> future = service.processData("data");
String result = future.get(); // Wait for result
```

---

### Garbage Collection in Spring Boot

**GC is handled by JVM, but you can configure:**

**JVM Options:**
```bash
java -Xmx512m -Xms256m -XX:+UseG1GC -jar myapp.jar
```

**GC Types:**
- **Serial GC** - Single thread, small apps
- **Parallel GC** - Multiple threads, throughput
- **G1 GC** - Low latency, large heaps (recommended)
- **ZGC** - Very low latency, large heaps

**Monitor GC:**
```bash
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar myapp.jar
```

**Spring Boot Actuator (GC Metrics):**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
management:
  endpoints:
    web:
      exposure:
        include: metrics,health
```

Access: `http://localhost:8080/actuator/metrics/jvm.gc.pause`

---

## Microservices with Spring Boot

### Introduction

**What are Microservices?**
- Small, independent services
- Each service has its own database
- Communicate via APIs
- Deploy independently

**Benefits:**
- Scalability
- Technology diversity
- Independent deployment
- Fault isolation

**Spring Boot for Microservices:**
- Lightweight
- Embedded server
- Easy to deploy
- Production-ready

---

### Communication Between Spring Microservices

#### 1. REST (Synchronous)
```java
@RestController
public class UserService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Order getOrder(String orderId) {
        return restTemplate.getForObject(
            "http://order-service/orders/" + orderId,
            Order.class
        );
    }
}
```

#### 2. Feign Client (Declarative REST)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

```java
@FeignClient(name = "order-service", url = "http://localhost:8081")
public interface OrderClient {
    @GetMapping("/orders/{id}")
    Order getOrder(@PathVariable String id);
}

// Usage
@Autowired
private OrderClient orderClient;

public Order getOrder(String id) {
    return orderClient.getOrder(id);
}
```

#### 3. Message Queue (Asynchronous)
- RabbitMQ
- Apache Kafka
- Amazon SQS

---

### Deploy Java Microservices on AWS Elastic Beanstalk

**Steps:**
1. Build JAR: `mvn clean package`
2. Create Elastic Beanstalk application
3. Upload JAR file
4. Configure environment variables
5. Deploy

**Dockerfile (Alternative):**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/myapp.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

### Project: Microservices Sample Project

**Structure:**
```
microservices/
├── api-gateway/          # API Gateway
├── user-service/         # User microservice
├── order-service/        # Order microservice
├── product-service/      # Product microservice
└── discovery-server/     # Service discovery
```

**Each Service:**
- Independent Spring Boot app
- Own database
- REST APIs
- Dockerized

---

## Spring Boot with Kafka

### Introduction

**Apache Kafka:**
- Distributed messaging system
- Publish-subscribe model
- High throughput
- Fault-tolerant

**Use Cases:**
- Event streaming
- Real-time data pipelines
- Log aggregation
- Microservices communication

---

### Kafka Producer and Consumer in Spring Boot

**Add Dependency:**
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

**Configuration:**
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      group-id: my-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

**Producer:**
```java
@Service
public class KafkaProducer {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
    
    public void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
    }
}
```

**Consumer:**
```java
@Service
public class KafkaConsumer {
    
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Received: " + message);
    }
}
```

---

### Publishing and Consuming JSON Messages

**JSON Producer:**
```java
@Service
public class JsonKafkaProducer {
    
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;
    
    public void sendUser(User user) {
        kafkaTemplate.send("user-topic", user);
    }
}
```

**JSON Consumer:**
```java
@Service
public class JsonKafkaConsumer {
    
    @KafkaListener(topics = "user-topic", groupId = "user-group")
    public void consumeUser(User user) {
        System.out.println("Received user: " + user.getName());
    }
}
```

**Configuration:**
```java
@Configuration
public class KafkaConfig {
    
    @Bean
    public ProducerFactory<String, User> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
    
    @Bean
    public KafkaTemplate<String, User> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

---

### Publishing and Consuming String Messages

**Simple String Producer:**
```java
@Service
public class StringProducer {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
```

**String Consumer:**
```java
@Service
public class StringConsumer {
    
    @KafkaListener(topics = "string-topic")
    public void consume(String message) {
        // Process string message
    }
}
```

---

### Create and Configure Topics in Apache Kafka

**Programmatic:**
```java
@Configuration
public class KafkaTopicConfig {
    
    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name("user-topic")
            .partitions(3)
            .replicas(1)
            .build();
    }
}
```

**Manual (Kafka CLI):**
```bash
# Create topic
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --topic my-topic \
  --partitions 3 \
  --replication-factor 1

# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Describe topic
kafka-topics.sh --describe \
  --bootstrap-server localhost:9092 \
  --topic my-topic
```

---

### Consume Message Through Kafka, Save into ElasticSearch and Plot into Grafana

**Flow:**
```
Kafka → Spring Boot Consumer → ElasticSearch → Grafana
```

**Consumer to ElasticSearch:**
```java
@Service
public class KafkaToElasticSearchConsumer {
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    
    @KafkaListener(topics = "logs-topic")
    public void consumeAndSave(LogMessage log) {
        // Save to ElasticSearch
        elasticsearchTemplate.save(log);
    }
}
```

**Grafana:**
- Connect to ElasticSearch
- Create dashboards
- Visualize data

---

### Start/Stop a Kafka Listener Dynamically

**Dynamic Listener Management:**
```java
@Service
public class DynamicKafkaListener {
    
    @Autowired
    private KafkaListenerEndpointRegistry registry;
    
    public void startListener(String listenerId) {
        MessageListenerContainer container = registry.getListenerContainer(listenerId);
        if (container != null && !container.isRunning()) {
            container.start();
        }
    }
    
    public void stopListener(String listenerId) {
        MessageListenerContainer container = registry.getListenerContainer(listenerId);
        if (container != null && container.isRunning()) {
            container.stop();
        }
    }
}
```

**Usage:**
```java
@RestController
public class ListenerController {
    
    @Autowired
    private DynamicKafkaListener listener;
    
    @PostMapping("/listeners/{id}/start")
    public void start(@PathVariable String id) {
        listener.startListener(id);
    }
    
    @PostMapping("/listeners/{id}/stop")
    public void stop(@PathVariable String id) {
        listener.stopListener(id);
    }
}
```

---

## 🎯 Summary

This guide covers:
- ✅ Spring Boot basics
- ✅ IoC and DI
- ✅ REST API development
- ✅ Advanced features (Scheduling, Email, Files, Caching, Transactions)
- ✅ Background/Async tasks
- ✅ Microservices
- ✅ Kafka integration

**All topics explained with examples! 🚀**

