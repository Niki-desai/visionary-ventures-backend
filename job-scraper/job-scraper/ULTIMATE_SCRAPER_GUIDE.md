# Ultimate Job Scraper - Setup Guide

## 🎯 This Will Get You 1000+ Jobs!

This scraper uses **3 powerful APIs** + **web scraping** to get maximum jobs.

## 🔑 API Keys Needed

### 1. ✅ Google API (You Already Have!)
```
GOOGLE_API_KEY=AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU
```
Already in your `.env` file!

### 2. 🆓 SerpAPI (HIGHLY RECOMMENDED - 100 Free Searches!)
**Why**: Best for scraping Google Jobs results

**How to get**:
1. Go to: https://serper.dev
2. Sign up (free)
3. Get API key from dashboard
4. Add to `.env`:
   ```
   SERPER_API_KEY=your_serper_key_here
   ```

**Free Tier**: 100 searches/month (enough for ~500+ jobs)

### 3. 🔵 Bing Web Search API (Optional)
**Why**: Access Bing's job search results

**How to get**:
1. Go to: https://portal.azure.com
2. Create free Azure account
3. Create "Bing Search v7" resource
4. Copy API key
5. Add to `.env`:
   ```
   BING_API_KEY=your_bing_key_here
   ```

**Free Tier**: 1000 searches/month

## 📊 Expected Results

With all APIs configured:

| Source | Expected Jobs |
|--------|--------------|
| SerpAPI (Google) | 300-500 jobs |
| Bing API | 200-400 jobs |
| Naukri.com | 200-300 jobs |
| RemoteOK | 50-100 jobs |
| LinkedIn | 50-100 jobs |
| **TOTAL** | **800-1400 jobs** |

## 🚀 How to Run

### Step 1: Get SerpAPI Key (5 minutes)
```bash
# 1. Go to https://serper.dev
# 2. Sign up with Google
# 3. Copy API key
# 4. Add to .env file
```

### Step 2: Update .env File
Open `.env` and add:
```bash
MONGODB_URI=mongodb://localhost:27017/jobbot
MONGODB_DATABASE=jobbot
GOOGLE_API_KEY=AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU
SERPER_API_KEY=your_serper_key_here  # ← Add this!
BING_API_KEY=your_bing_key_here      # ← Optional
```

### Step 3: Run the Ultimate Scraper
```bash
powershell -ExecutionPolicy Bypass -Command "node scraper-ultimate.js"
```

## 🎁 What You Get

### Locations Covered:
- Pune
- Mumbai
- Bangalore
- Hyderabad
- Delhi
- Remote (Worldwide)

### Job Types:
- MERN Stack Developer
- Node.js Developer
- Fullstack MERN
- DevOps Engineer
- React Developer
- Backend Developer

### Job Sources:
1. **Google Jobs** (via SerpAPI) - Most jobs!
2. **Bing Jobs** (via Bing API)
3. **Naukri.com** (India's #1 job site)
4. **RemoteOK**
5. **LinkedIn**
6. **Indeed**

## 💡 Pro Tips

1. **Start with SerpAPI**: Just get this one key and you'll get 300-500 jobs immediately!
2. **Run multiple times**: APIs have daily limits, so run once per day
3. **Use filters**: Query the database to find exactly what you want

## 🔍 Quick Start (Just SerpAPI)

Minimum setup to get 300+ jobs:

1. Get SerpAPI key: https://serper.dev (2 minutes)
2. Add to `.env`: `SERPER_API_KEY=your_key`
3. Run: `node scraper-ultimate.js`
4. Get 300-500 jobs! 🎉

## 📝 Current Status

- ✅ Google API Key: Ready
- ⏳ SerpAPI Key: **Get this now!** (https://serper.dev)
- ⏳ Bing API Key: Optional (for even more jobs)

**Next Step**: Get SerpAPI key and run `scraper-ultimate.js` to get 500+ jobs!
