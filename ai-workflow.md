## 🌟 High-Level User Journey (9-Step Flow)

> **Goal**: User says “I want a job as a Python backend dev in Bangalore with ₹90k+ salary” → system finds good jobs, tailors resume, and helps apply.

---

### ✅ Step 1: User Signs Up & Uploads Resume  
- **What happens**:  
  - User creates account (`User` saved in MongoDB)  
  - Uploads a PDF/DOC resume → system extracts raw text  
- **Tech**:  
  - `POST /auth/register`  
  - `POST /resumes` → saves `Resume` doc  
  - **AI**: Not used yet

---

### ✅ Step 2: User Starts a “Job Search Session”  
- **What happens**:  
  - User types a message like:  
    > “Find me remote Python backend jobs in India paying ₹90K+”  
  - System creates a **Workflow** (a “search session”)  
- **Tech**:  
  - `POST /workflows` → creates `Workflow` + `WorkflowStep(INTENT_PARSE)`

---

### ✅ Step 3: AI Parses User’s Intent  
- **What happens**:  
  - AI (e.g., GPT-4o) reads the user message and extracts structured data:  
    ```json
    { "role": "Backend Engineer", "techStack": ["Python", "FastAPI"], "location": "India", "salaryMin": 90000 }
    ```  
- **Tech**:  
  - `AgentWorkflowOrchestrator` → calls `handleIntentParse()`  
  - Saves result in `Workflow.intent` + logs to `AiLog`

---

### ✅ Step 4: AI Extracts Skills from Resume  
- **What happens**:  
  - System sends resume text to AI → gets clean list of skills and experience  
  - Saves into `Resume.parsed`  
- **Tech**:  
  - `ResumeParsingService` → uses OpenAI  
  - Skipped if resume already parsed (uses `fileContentHash`)

---

### ✅ Step 5: System Searches for Matching Jobs  
- **What happens**:  
  - Uses **vector search** in MongoDB to find jobs **similar** to user’s profile + intent  
  - Also filters by location, salary, etc.  
- **Tech**:  
  - `JobMatchingService` → Atlas Vector Search on `JobPosting.embedding`  
  - Returns top 10 ranked jobs

---

### ✅ Step 6: AI Ranks & Explains Matches  
- **What happens**:  
  - AI scores jobs and may explain *why* a job is a good fit  
  - (Optional: shown to user in UI)  
- **Tech**:  
  - Logged as `WorkflowStep(JOB_RANK)`

---

### ✅ Step 7: AI Tailors Resume for a Specific Job  
- **What happens**:  
  - When user picks a job, system generates a **custom resume** just for that role  
  - Highlights relevant skills, rewrites summary  
- **Tech**:  
  - `ResumeTailoringService` → calls OpenAI with job + resume  
  - Saves as `CustomResume`

---

### ✅ Step 8: System Prepares Application Form  
- **What happens**:  
  - AI looks at the job application page and maps user data to form fields like:  
    ```json
    { "fullName": "Alex", "email": "a@example.com", "coverLetter": "..." }
    ```  
  - **Does NOT auto-submit yet** (safety first!)  
- **Tech**:  
  - `ApplicationSubmissionService.prepareSubmission()` → returns `SubmissionPlan`  
  - Actual submit done by **external Playwright service** (separate microservice)

---

### ✅ Step 9: Application Is Tracked Over Time  
- **What happens**:  
  - System logs: “Application submitted on [date]”  
  - Every day, it checks if status changed (e.g., “Viewed”, “Interview”)  
- **Tech**:  
  - `ApplicationTrackingService` → runs daily cron  
  - Updates `Application.status`

---

## 🔁 Behind the Scenes: System Maintenance

- **Every 4 hours**:  
  - `JobIngestionService` fetches new jobs from Indeed/LinkedIn → saves as `JobPosting`  
  - Generates **embeddings** (vectors) for each job using OpenAI → stored in MongoDB

- **All AI calls**:  
  - Logged in `AiLog` for debugging, cost tracking, and improving prompts

- **Errors**:  
  - If a step fails (e.g., OpenAI timeout), system retries up to 3 times  
  - If still fails → logs to error queue, alerts team

---

## 🧠 Visual Summary (Text Diagram)

```
User → [Register + Upload Resume]
         ↓
     [Start Workflow] → AI parses intent
         ↓
     [Parse Resume] → AI extracts skills
         ↓
     [Search Jobs] → Vector search + filters
         ↓
     [Rank Jobs] → AI scores & explains
         ↓
User picks job → [Tailor Resume] → AI customizes
         ↓
     [Prepare Form] → AI maps fields
         ↓
[Submit via Playwright*] → (external service)
         ↓
[Track Status Daily] → Update application state
```

> \* Actual clicking/filling done by a **separate automation service** — not in Spring Boot app.

---

## 📦 Data Flow (Key Collections)

| Action | MongoDB Collection Updated |
|-------|----------------------------|
| Sign up | `User` |
| Upload resume | `Resume` |
| Start search | `Workflow`, `WorkflowStep` |
| Find jobs | Reads `JobPosting` (written by ingestion job) |
| Customize resume | `CustomResume` |
| Submit app | `Application` |
| Log AI | `AiLog` |

---
