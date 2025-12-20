// Enhanced scraper with delays and retry logic
import puppeteer from 'puppeteer';
import mongoose from 'mongoose';
import { connectDB, Job, saveJobs } from './database.js';

// Enhanced Search configurations
const LOCATIONS = ["Pune", "Remote", "Mumbai", "Bangalore", "Hyderabad"];
const JOB_TYPES = ["Full-time", "Part-time", "Freelance", "Contract"];
const EXPERIENCE = "2-4 years";

const SEARCH_CONFIGS = [
    { query: "MERN stack developer", tag: "MERN", keywords: ["MERN", "MongoDB", "Express", "React", "Node.js"] },
    { query: "Node.js developer", tag: "Node.js", keywords: ["Node.js", "NodeJS", "Backend", "JavaScript"] },
    { query: "Fullstack MERN developer", tag: "Fullstack MERN", keywords: ["Fullstack", "Full-stack", "MERN", "React", "Node.js"] },
    { query: "DevOps engineer", tag: "DevOps", keywords: ["DevOps", "CI/CD", "Docker", "Kubernetes", "AWS"] },
    { query: "React developer", tag: "React", keywords: ["React", "ReactJS", "Frontend", "JavaScript"] },
    { query: "Backend Node.js", tag: "Backend", keywords: ["Backend", "Node.js", "API", "Server"] }
];

// Delay helper
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// Build search query
function buildSearchQuery(config, location) {
    return [config.query, location, EXPERIENCE].filter(Boolean).join(" ");
}

// Enhanced Indeed scraper with retry
async function scrapeIndeedEnhanced(browser, searchQuery, tag, location, retries = 3) {
    for (let attempt = 1; attempt <= retries; attempt++) {
        try {
            console.log(`\n🔍 Scraping Indeed for ${tag}... (Attempt ${attempt}/${retries})`);

            const page = await browser.newPage();

            // Randomize user agent
            const userAgents = [
                'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
                'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
                'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
            ];
            await page.setUserAgent(userAgents[Math.floor(Math.random() * userAgents.length)]);

            // Set extra headers
            await page.setExtraHTTPHeaders({
                'Accept-Language': 'en-US,en;q=0.9',
                'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
            });

            const url = `https://www.indeed.com/jobs?q=${encodeURIComponent(searchQuery)}&l=${encodeURIComponent(location)}`;
            await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

            // Wait for job cards to load
            await page.waitForSelector('.job_seen_beacon, .jobsearch-SerpJobCard, div[data-jk]', { timeout: 10000 }).catch(() => { });

            const jobs = await page.evaluate((tag) => {
                const jobCards = document.querySelectorAll('.job_seen_beacon, .jobsearch-SerpJobCard, div[data-jk], .resultContent');
                const results = [];

                jobCards.forEach(card => {
                    const title = card.querySelector('h2.jobTitle, a.jcs-JobTitle span, h2 span')?.textContent?.trim();
                    const company = card.querySelector('.companyName, span.companyName')?.textContent?.trim();
                    const location = card.querySelector('.companyLocation, div.companyLocation')?.textContent?.trim();
                    const link = card.querySelector('a.jcs-JobTitle, h2.jobTitle a')?.href;

                    if (title) {
                        results.push({
                            title,
                            company: company || 'N/A',
                            location: location || 'Remote',
                            link,
                            tag,
                            source: 'Indeed'
                        });
                    }
                });

                return results;
            }, tag);

            await page.close();
            console.log(`✅ Found ${jobs.length} jobs from Indeed`);

            if (jobs.length > 0 || attempt === retries) {
                return jobs;
            }

            // Wait before retry
            await delay(3000 * attempt);

        } catch (error) {
            console.error(`❌ Attempt ${attempt} failed: ${error.message}`);
            if (attempt === retries) {
                return [];
            }
            await delay(5000 * attempt);
        }
    }
    return [];
}

// Main scraper with delays
async function scrapeAllJobsEnhanced() {
    console.log("🚀 Starting ENHANCED Multi-Source Job Scraper...\n");
    console.log("=".repeat(60));

    await connectDB();

    const browser = await puppeteer.launch({
        headless: true,
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-blink-features=AutomationControlled',
            '--disable-web-security'
        ]
    });
    console.log("✅ Browser launched\n");

    let totalNewJobs = 0;

    for (const location of LOCATIONS) {
        console.log(`\n🌍 Searching in: ${location}`);
        console.log("=".repeat(60));

        for (const config of SEARCH_CONFIGS) {
            const searchQuery = buildSearchQuery(config, location);

            console.log(`\n📍 Searching for: "${config.query}" in ${location}`);

            // Scrape Indeed with enhanced retry logic
            const indeedJobs = await scrapeIndeedEnhanced(browser, searchQuery, config.tag, location);

            // Add delay between searches
            await delay(2000);

            // Add location info
            const allJobs = indeedJobs.map(job => ({
                ...job,
                location: job.location || location,
                searchLocation: location,
                experience: EXPERIENCE,
                jobTypes: JOB_TYPES
            }));

            if (allJobs.length > 0) {
                const stats = await saveJobs(allJobs);
                totalNewJobs += stats.savedCount;
                console.log(`✅ Saved ${stats.savedCount} new jobs (${stats.duplicateCount} duplicates)`);
            }

            // Delay between different job types
            await delay(3000);
        }

        // Longer delay between locations
        await delay(5000);
    }

    await browser.close();
    console.log("\n🌐 Browser closed");

    console.log("\n" + "=".repeat(60));
    console.log(`🎉 Scraping completed! Total new jobs: ${totalNewJobs}`);
    console.log("=".repeat(60));

    // Show final stats
    const totalJobs = await Job.countDocuments();
    console.log(`\n📈 Total jobs in database: ${totalJobs}`);

    await mongoose.connection.close();
    console.log("\n✅ Database connection closed");
}

scrapeAllJobsEnhanced().catch(error => {
    console.error("Fatal error:", error);
    process.exit(1);
});
