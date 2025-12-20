# Enhanced Job Scraper - Search Criteria Summary

## 🎯 What's Configured

### Locations
- **Pune** (India)
- **Remote** (Worldwide)

### Job Types
- Full-time
- Part-time
- Freelance
- Contract

### Experience Level
- **2-4 years**

### Roles/Tags
1. **MERN Stack Developer**
   - Keywords: MERN, MongoDB, Express, React, Node.js

2. **Node.js Developer**
   - Keywords: Node.js, NodeJS, Backend, JavaScript

3. **Fullstack MERN Developer**
   - Keywords: Fullstack, Full-stack, MERN, React, Node.js

4. **DevOps Engineer**
   - Keywords: DevOps, CI/CD, Docker, Kubernetes, AWS

## 📊 Search Matrix

The scraper will run **8 searches** (2 locations × 4 roles):

| Location | Role | Full Query |
|----------|------|------------|
| Pune | MERN | "MERN stack developer Pune 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Pune | Node.js | "Node.js developer Pune 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Pune | Fullstack MERN | "Fullstack MERN developer Pune 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Pune | DevOps | "DevOps engineer Pune 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Remote | MERN | "MERN stack developer Remote 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Remote | Node.js | "Node.js developer Remote 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Remote | Fullstack MERN | "Fullstack MERN developer Remote 2-4 years Full-time OR Part-time OR Freelance OR Contract" |
| Remote | DevOps | "DevOps engineer Remote 2-4 years Full-time OR Part-time OR Freelance OR Contract" |

## 🗄️ Database Schema

Each job saved will have:
```javascript
{
  title: String,
  company: String,
  location: String,              // Actual job location from listing
  searchLocation: String,        // "Pune" or "Remote" (what you searched for)
  link: String,
  description: String,
  source: String,                // LinkedIn, Indeed, RemoteOK, Glassdoor, WeWorkRemotely
  tag: String,                   // MERN, Node.js, Fullstack MERN, DevOps
  experience: String,            // "2-4 years"
  jobTypes: [String],            // ["Full-time", "Part-time", "Freelance", "Contract"]
  scrapedAt: Date,
  createdAt: Date,
  updatedAt: Date
}
```

## 🚀 How to Run

### Recommended: Puppeteer Scraper
```bash
powershell -ExecutionPolicy Bypass -Command "node scraper-puppeteer.js"
```

### Alternative: Basic Scraper
```bash
powershell -ExecutionPolicy Bypass -Command "npm start"
```

## 📈 Expected Results

With 8 searches across 5 job boards, you could potentially find:
- **40 searches total** (8 queries × 5 sources)
- Estimated: **50-200 unique jobs** (depending on availability)

## 🔍 Query Examples

After scraping, you can query the database:

```javascript
// All jobs in Pune
db.jobs.find({ searchLocation: "Pune" })

// DevOps jobs with 2-4 years experience
db.jobs.find({ tag: "DevOps", experience: "2-4 years" })

// Freelance MERN jobs
db.jobs.find({ 
  tag: "MERN", 
  jobTypes: "Freelance" 
})

// Remote Fullstack jobs from LinkedIn
db.jobs.find({ 
  searchLocation: "Remote",
  tag: "Fullstack MERN",
  source: "LinkedIn"
})
```

## ⚙️ Customization

To modify search criteria, edit these variables in `scraper.js` or `scraper-puppeteer.js`:

```javascript
const LOCATIONS = ["Pune", "Remote"];  // Add more cities
const JOB_TYPES = ["Full-time", "Part-time", "Freelance", "Contract"];
const EXPERIENCE = "2-4 years";  // Change experience level

const SEARCH_CONFIGS = [
  // Add more role configurations here
];
```
