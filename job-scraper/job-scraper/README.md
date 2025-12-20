# Job Scraper - Multi-Source MERN & Node.js Jobs

## Overview
This job scraper collects MERN stack and Node.js developer positions from 5 major job boards and stores them in a single MongoDB collection with source differentiation.

## Features
- ✅ **5 Job Sources**: LinkedIn, Indeed, RemoteOK, Glassdoor, WeWorkRemotely
- ✅ **Single Collection**: All jobs stored in `jobs` collection
- ✅ **Source Differentiation**: `source` field identifies the job board
- ✅ **Tag System**: `tag` field differentiates MERN vs Node.js roles
- ✅ **Duplicate Detection**: Prevents duplicate entries based on title, company, and source
- ✅ **Multiple Scraping Methods**:
  - **Web Scraping** (Axios + Cheerio) - Free, basic
  - **Browser Automation** (Puppeteer) - More reliable
  - **API-based** (Google API, SerpAPI, Indeed API) - Most reliable, requires API keys

## Database Schema

```javascript
{
  title: String,           // Job title
  company: String,         // Company name
  location: String,        // Job location
  link: String,            // Job posting URL
  description: String,     // Job description (optional)
  source: String,          // One of: LinkedIn, Indeed, RemoteOK, Glassdoor, WeWorkRemotely
  tag: String,             // Either "MERN" or "Node.js"
  scrapedAt: Date,         // When the job was scraped
  createdAt: Date,         // Auto-generated
  updatedAt: Date          // Auto-generated
}
```

## Installation

```bash
cd job-scraper/job-scraper
powershell -ExecutionPolicy Bypass -Command "npm install"
```

## Usage

### Basic Scraper (Axios + Cheerio)
```bash
powershell -ExecutionPolicy Bypass -Command "npm start"
```

### Enhanced Scraper (Puppeteer - Recommended)
```bash
powershell -ExecutionPolicy Bypass -Command "node scraper-puppeteer.js"
```

### API-based Scraper (Most Reliable - Requires Setup)
```bash
powershell -ExecutionPolicy Bypass -Command "node scraper-api.js"
```

**Note**: For the API scraper, you need to set up a Google Custom Search Engine ID. See [GOOGLE_API_SETUP.md](file:///c:/Users/nikit/Downloads/Nikita/Prepare/visionar-ventures-backend/job-scraper/job-scraper/GOOGLE_API_SETUP.md) for instructions.

## Configuration

Edit the search queries in either `scraper.js` or `scraper-puppeteer.js`:

```javascript
const SEARCH_CONFIGS = [
    { query: "MERN stack developer", tag: "MERN" },
    { query: "Node.js developer", tag: "Node.js" }
];
```

## MongoDB Connection

The scraper uses the existing MongoDB connection:
- **URI**: `mongodb://localhost:27017/jobbot`
- **Database**: `jobbot`
- **Collection**: `jobs`

## Querying Jobs

### Get all MERN jobs
```javascript
db.jobs.find({ tag: "MERN" })
```

### Get all Node.js jobs from LinkedIn
```javascript
db.jobs.find({ tag: "Node.js", source: "LinkedIn" })
```

### Get jobs by source
```javascript
db.jobs.find({ source: "Indeed" })
```

### Count jobs by tag
```javascript
db.jobs.countDocuments({ tag: "MERN" })
db.jobs.countDocuments({ tag: "Node.js" })
```

## Files

- **`database.js`**: MongoDB connection and schema definition
- **`scraper.js`**: Basic scraper using Axios and Cheerio
- **`scraper-puppeteer.js`**: Enhanced scraper using Puppeteer (recommended for web scraping)
- **`scraper-api.js`**: API-based scraper using Google Custom Search and other APIs (most reliable)
- **`package.json`**: Dependencies and scripts
- **`.env`**: Environment variables including Google API key (gitignored)
- **`GOOGLE_API_SETUP.md`**: Guide for setting up Google Custom Search Engine

## Notes

- The Puppeteer version is more reliable as it renders JavaScript and bypasses basic bot detection
- Some websites may still block requests - this is normal
- Run the scraper periodically to keep job listings up to date
- Duplicates are automatically skipped based on title + company + source
