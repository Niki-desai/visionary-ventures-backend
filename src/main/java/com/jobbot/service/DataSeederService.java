package com.jobbot.service;

import com.jobbot.model.*;
import com.jobbot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class DataSeederService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobSearchRepository jobSearchRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AIConversationRepository aiConversationRepository;

    @Override
    public void run(String... args) {
        // Seed data on application startup
        System.out.println("🚀 Starting data seeding...");
        try {
            seedData();
        } catch (Exception e) {
            System.err.println("❌ Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void seedData() {
        System.out.println("🌱 Seeding MongoDB database...");
        
        // Check if data already exists
        long userCount = userRepository.count();
        if (userCount > 0) {
            System.out.println("ℹ️  Data already exists (" + userCount + " users). Skipping seed.");
            return;
        }

        // Clear existing data (optional - be careful in production!)
        // clearAllData();

        // Seed Users
        User user = createSampleUser();
        userRepository.save(user);
        System.out.println("✅ Created sample user: " + user.getEmail());

        // Seed Jobs
        List<Job> jobs = createSampleJobs();
        jobRepository.saveAll(jobs);
        System.out.println("✅ Created " + jobs.size() + " sample jobs");

        // Seed Resume
        Resume resume = createSampleResume(user.getId());
        resumeRepository.save(resume);
        System.out.println("✅ Created sample resume");

        // Seed Job Search
        JobSearch jobSearch = createSampleJobSearch(user.getId());
        jobSearchRepository.save(jobSearch);
        System.out.println("✅ Created sample job search");

        // Seed Application
        if (!jobs.isEmpty()) {
            Application application = createSampleApplication(user.getId(), jobs.get(0).getId(), resume.getId());
            applicationRepository.save(application);
            System.out.println("✅ Created sample application");
        }

        // Seed AI Conversation
        AIConversation conversation = createSampleAIConversation(user.getId());
        aiConversationRepository.save(conversation);
        System.out.println("✅ Created sample AI conversation");

        System.out.println("🎉 Database seeding completed!");
    }

    private User createSampleUser() {
        User user = new User();
        user.setEmail("demo@jobbot.com");
        user.setPasswordHash("$2a$10$hashedpassword"); // In production, use proper hashing
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+1234567890");
        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setSubscriptionTier(User.SubscriptionTier.PREMIUM);
        
        User.UserPreferences preferences = new User.UserPreferences();
        preferences.setJobAlertsEnabled(true);
        preferences.setNotificationEmail(true);
        preferences.setAutoApplyEnabled(true);
        preferences.setPreferredLocations(Arrays.asList("San Francisco", "New York", "Remote"));
        preferences.setPreferredIndustries(Arrays.asList("Technology", "Software"));
        preferences.setMinSalary(100000);
        preferences.setMaxSalary(200000);
        preferences.setRemotePreference("hybrid");
        user.setPreferences(preferences);
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        
        return user;
    }

    private List<Job> createSampleJobs() {
        Job job1 = new Job();
        job1.setTitle("Senior Software Engineer");
        job1.setDescription("We are looking for a Senior Software Engineer with experience in Java and Spring Boot.");
        job1.setCompanyName("Tech Corp");
        job1.setCompanyId("company-001");
        
        Job.JobLocation location = new Job.JobLocation();
        location.setCity("San Francisco");
        location.setState("CA");
        location.setCountry("USA");
        location.setZipCode("94102");
        job1.setLocation(location);
        
        job1.setJobType("full-time");
        job1.setRemoteType("hybrid");
        
        Job.SalaryRange salary = new Job.SalaryRange();
        salary.setMin(120000);
        salary.setMax(180000);
        salary.setCurrency("USD");
        salary.setPeriod("yearly");
        job1.setSalary(salary);
        
        job1.setIndustry("Technology");
        job1.setRequiredSkills(Arrays.asList("Java", "Spring Boot", "MongoDB", "REST API"));
        job1.setPreferredSkills(Arrays.asList("AWS", "Docker", "Kubernetes"));
        job1.setExperienceLevel("senior");
        job1.setEducationRequirements(Arrays.asList("Bachelor's in Computer Science"));
        
        Job.JobSource source = new Job.JobSource();
        source.setName("linkedin");
        source.setApiProvider("LinkedIn API");
        source.setScraped(false);
        job1.setSource(source);
        
        job1.setExternalUrl("https://linkedin.com/jobs/view/123456");
        job1.setApplicationUrl("https://techcorp.com/careers/apply/123456");
        job1.setPostedDate(LocalDateTime.now().minusDays(5));
        job1.setExpiryDate(LocalDateTime.now().plusDays(30));
        job1.setIsActive(true);
        job1.setApplicationCount(0);
        
        Job.AIAnalysis aiAnalysis = new Job.AIAnalysis();
        aiAnalysis.setMatchScore(0.85);
        aiAnalysis.setSkillMatchPercentage(0.90);
        aiAnalysis.setRecommended(true);
        aiAnalysis.setReasoning("Strong match: 90% skill overlap, good salary range, hybrid work option");
        aiAnalysis.setAnalyzedAt(LocalDateTime.now());
        job1.setAiAnalysis(aiAnalysis);
        
        job1.setCreatedAt(LocalDateTime.now());
        job1.setUpdatedAt(LocalDateTime.now());

        Job job2 = new Job();
        job2.setTitle("Full Stack Developer");
        job2.setDescription("Join our team as a Full Stack Developer working with modern technologies.");
        job2.setCompanyName("StartupXYZ");
        job2.setCompanyId("company-002");
        
        Job.JobLocation location2 = new Job.JobLocation();
        location2.setCity("New York");
        location2.setState("NY");
        location2.setCountry("USA");
        job2.setLocation(location2);
        
        job2.setJobType("full-time");
        job2.setRemoteType("remote");
        
        Job.SalaryRange salary2 = new Job.SalaryRange();
        salary2.setMin(100000);
        salary2.setMax(150000);
        salary2.setCurrency("USD");
        salary2.setPeriod("yearly");
        job2.setSalary(salary2);
        
        job2.setIndustry("Technology");
        job2.setRequiredSkills(Arrays.asList("JavaScript", "React", "Node.js", "MongoDB"));
        job2.setExperienceLevel("mid");
        job2.setIsActive(true);
        job2.setPostedDate(LocalDateTime.now().minusDays(2));
        job2.setExpiryDate(LocalDateTime.now().plusDays(25));
        job2.setCreatedAt(LocalDateTime.now());
        job2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(job1, job2);
    }

    private Resume createSampleResume(String userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle("Software Engineer Resume");
        resume.setIsDefault(true);
        
        Resume.PersonalInfo personalInfo = new Resume.PersonalInfo();
        personalInfo.setFullName("John Doe");
        personalInfo.setEmail("demo@jobbot.com");
        personalInfo.setPhone("+1234567890");
        personalInfo.setLinkedinUrl("https://linkedin.com/in/johndoe");
        personalInfo.setGithubUrl("https://github.com/johndoe");
        resume.setPersonalInfo(personalInfo);
        
        resume.setSummary("Experienced Software Engineer with 5+ years in Java and Spring Boot development.");
        
        Resume.Experience exp1 = new Resume.Experience();
        exp1.setCompany("Previous Company");
        exp1.setPosition("Software Engineer");
        exp1.setStartDate("2020-01");
        exp1.setEndDate("2023-12");
        exp1.setIsCurrent(false);
        exp1.setDescription("Developed and maintained microservices using Spring Boot");
        exp1.setAchievements(Arrays.asList("Led team of 3 developers", "Improved system performance by 40%"));
        
        resume.setExperience(Arrays.asList(exp1));
        
        Resume.Education edu1 = new Resume.Education();
        edu1.setInstitution("University of Technology");
        edu1.setDegree("Bachelor of Science");
        edu1.setFieldOfStudy("Computer Science");
        edu1.setStartDate("2016");
        edu1.setEndDate("2020");
        edu1.setGpa("3.8");
        resume.setEducation(Arrays.asList(edu1));
        
        resume.setSkills(Arrays.asList("Java", "Spring Boot", "MongoDB", "REST API", "AWS", "Docker"));
        resume.setFileFormat("pdf");
        resume.setAiEnhanced(false);
        resume.setVersion(1);
        resume.setCreatedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());
        
        return resume;
    }

    private JobSearch createSampleJobSearch(String userId) {
        JobSearch search = new JobSearch();
        search.setUserId(userId);
        search.setName("Software Engineer Jobs - SF/NY");
        search.setIsActive(true);
        
        JobSearch.SearchCriteria criteria = new JobSearch.SearchCriteria();
        criteria.setKeywords(Arrays.asList("software engineer", "java developer"));
        criteria.setJobTitles(Arrays.asList("Senior Software Engineer", "Full Stack Developer"));
        criteria.setLocations(Arrays.asList("San Francisco", "New York", "Remote"));
        criteria.setRemoteType("hybrid");
        criteria.setJobTypes(Arrays.asList("full-time"));
        criteria.setIndustries(Arrays.asList("Technology"));
        criteria.setExperienceLevel("senior");
        criteria.setSalaryMin(100000);
        criteria.setSalaryMax(200000);
        criteria.setRequiredSkills(Arrays.asList("Java", "Spring Boot", "MongoDB"));
        search.setSearchCriteria(criteria);
        
        JobSearch.AlertSettings alertSettings = new JobSearch.AlertSettings();
        alertSettings.setEnabled(true);
        alertSettings.setFrequency("daily");
        alertSettings.setEmailNotifications(true);
        alertSettings.setSmsNotifications(false);
        alertSettings.setMaxResultsPerAlert(10);
        search.setAlertSettings(alertSettings);
        
        search.setLastSearchedAt(LocalDateTime.now());
        search.setResultsCount(15);
        search.setCreatedAt(LocalDateTime.now());
        search.setUpdatedAt(LocalDateTime.now());
        
        return search;
    }

    private Application createSampleApplication(String userId, String jobId, String resumeId) {
        Application application = new Application();
        application.setUserId(userId);
        application.setJobId(jobId);
        application.setResumeId(resumeId);
        
        Application.ApplicationStatus status = new Application.ApplicationStatus();
        status.setCurrent("submitted");
        status.setLastUpdated(LocalDateTime.now());
        application.setStatus(status);
        
        application.setCoverLetter("Dear Hiring Manager, I am excited to apply for the Senior Software Engineer position...");
        application.setCoverLetterGenerated(false);
        application.setApplicationMethod("manual");
        application.setSubmittedAt(LocalDateTime.now().minusDays(2));
        
        Application.AIInsights insights = new Application.AIInsights();
        insights.setMatchScore(0.88);
        insights.setStrengths(Arrays.asList("Strong Java experience", "Relevant Spring Boot skills"));
        insights.setWeaknesses(Arrays.asList("Limited AWS experience"));
        insights.setSuggestions(Arrays.asList("Highlight microservices experience", "Emphasize team leadership"));
        insights.setCoverLetterQualityScore(0.82);
        insights.setAnalyzedAt(LocalDateTime.now());
        application.setAiInsights(insights);
        
        application.setCreatedAt(LocalDateTime.now().minusDays(3));
        application.setUpdatedAt(LocalDateTime.now());
        
        return application;
    }

    private AIConversation createSampleAIConversation(String userId) {
        AIConversation conversation = new AIConversation();
        conversation.setUserId(userId);
        conversation.setConversationType("job_search");
        conversation.setIsActive(true);
        
        AIConversation.ConversationContext context = new AIConversation.ConversationContext();
        context.setUserPreferences(java.util.Map.of("preferredLocation", "San Francisco"));
        conversation.setContext(context);
        
        AIConversation.Message userMsg = new AIConversation.Message();
        userMsg.setRole("user");
        userMsg.setContent("Help me find software engineer jobs in San Francisco");
        userMsg.setTimestamp(LocalDateTime.now().minusHours(2));
        userMsg.setModelUsed("gpt-4");
        
        AIConversation.Message aiMsg = new AIConversation.Message();
        aiMsg.setRole("assistant");
        aiMsg.setContent("I found 15 software engineer positions matching your criteria. Here are the top 5...");
        aiMsg.setTimestamp(LocalDateTime.now().minusHours(2));
        aiMsg.setTokensUsed(250);
        aiMsg.setModelUsed("gpt-4");
        
        conversation.setMessages(Arrays.asList(userMsg, aiMsg));
        conversation.setSummary("User requested help finding software engineer jobs in San Francisco");
        conversation.setCreatedAt(LocalDateTime.now().minusHours(2));
        conversation.setUpdatedAt(LocalDateTime.now());
        
        return conversation;
    }

    // Optional: Clear all data (use with caution!)
    private void clearAllData() {
        userRepository.deleteAll();
        jobRepository.deleteAll();
        resumeRepository.deleteAll();
        jobSearchRepository.deleteAll();
        applicationRepository.deleteAll();
        aiConversationRepository.deleteAll();
        System.out.println("🗑️  Cleared all existing data");
    }
}

