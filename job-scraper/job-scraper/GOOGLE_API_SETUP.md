# Google API Setup Guide

## Your Google API Key
```
AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU
```

This key is already added to your `.env` file.

## ⚠️ Important: You Need a Custom Search Engine ID

To use Google Custom Search API for job searches, you need to create a **Custom Search Engine (CSE)**:

### Steps to Create Google Custom Search Engine:

1. **Go to**: https://programmablesearchengine.google.com/

2. **Click "Add"** to create a new search engine

3. **Configure**:
   - **Sites to search**: Enter `*.linkedin.com/jobs/*`, `*.indeed.com/*`, `*.glassdoor.com/*`
   - Or select "Search the entire web"
   - **Name**: "Job Search Engine"

4. **Get your Search Engine ID**:
   - After creation, click on your search engine
   - Click "Setup" → "Basics"
   - Copy the **Search Engine ID** (looks like: `017576662512468239146:omuauf_lfve`)

5. **Add to `.env` file**:
   ```
   GOOGLE_SEARCH_ENGINE_ID=your_search_engine_id_here
   ```

## Alternative: Use SerpAPI (Easier)

Instead of Google Custom Search, you can use **SerpAPI** which is specifically designed for scraping Google search results:

1. **Sign up**: https://serpapi.com/ (free tier: 100 searches/month)
2. **Get API key** from dashboard
3. **Add to `.env`**:
   ```
   SERPER_API_KEY=your_serper_key_here
   ```

## Environment Variables Summary

Your `.env` file should contain:

```bash
# MongoDB
MONGODB_URI=mongodb://localhost:27017/jobbot
MONGODB_DATABASE=jobbot

# Google APIs
GOOGLE_API_KEY=AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU
GOOGLE_SEARCH_ENGINE_ID=your_cse_id_here  # ← You need to create this

# Optional: Alternative APIs
SERPER_API_KEY=your_serper_key_here        # From serper.dev (easier alternative)
INDEED_PUBLISHER_ID=your_indeed_id_here    # From indeed.com/publisher
```

## Usage

Once you have the Search Engine ID:

```bash
powershell -ExecutionPolicy Bypass -Command "node scraper-api.js"
```

## API Limits

- **Google Custom Search**: 100 queries/day (free tier)
- **SerpAPI**: 100 searches/month (free tier)
- **Indeed API**: 1000 calls/day (free)

## Recommendation

For the best results, I recommend using **SerpAPI** (serper.dev) instead of Google Custom Search because:
- ✅ Easier setup (no CSE required)
- ✅ Better for job searches
- ✅ More reliable results
- ✅ 100 free searches/month
