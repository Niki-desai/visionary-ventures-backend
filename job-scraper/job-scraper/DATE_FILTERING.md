# Date Filtering - Last 30 Days Only

## ✅ What's Added

Your scraper now **only saves jobs from the last 30 days**!

### How It Works:

1. **Search Query Filter**
   - Adds `after:YYYY-MM-DD` to Google search
   - Example: `"MERN developer remote after:2024-11-20"`
   - Google only returns recent jobs

2. **Database Filter**
   - Checks `postedDate` field
   - Skips jobs older than 30 days
   - Saves only fresh jobs

3. **New Database Field**
   - Added `postedDate` to schema
   - Indexed for fast queries
   - Defaults to current date if not provided

## 📊 Stats You'll See

```
💾 Saved 15 new jobs
   Duplicates: 5
   Old jobs (>30 days): 3
```

## 🔍 Query Recent Jobs

```javascript
// Get all jobs from last 30 days
const thirtyDaysAgo = new Date();
thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

db.jobs.find({
  postedDate: { $gte: thirtyDaysAgo }
})

// Get last week's jobs
const oneWeekAgo = new Date();
oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

db.jobs.find({
  postedDate: { $gte: oneWeekAgo }
})
```

## ✅ Benefits

- ✅ Only fresh, recent jobs
- ✅ No outdated listings
- ✅ Better job application success rate
- ✅ Faster database queries with date index

**Your scraper now focuses on the freshest opportunities!** 🎯
