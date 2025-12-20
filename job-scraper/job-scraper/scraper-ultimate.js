// ULTIMATE Job Scraper - APIs + Web Scraping
import axios from 'axios';
import puppeteer from 'puppeteer';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import { connectDB, Job, saveJobs } from './database.js';

dotenv.config();

// API Keys
const GOOGLE_API_KEY = process.env.GOOGLE_API_KEY || 'AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU';
const SERPER_API_KEY = process.env.SERPER_API_KEY || "eb419d5d1ffffc208400c40fc78d0fdccdc727db"; // Get from serper.dev (100 free searches/month)
const BING_API_KEY = process.env.BING_API_KEY; // Get from Azure

// Enhanced Search configurations for Remote Jobs
const LOCATIONS = [
    // India - Remote & Major Cities
    "Remote India",
    "Pune India",
    "Mumbai India",
    "Bangalore India",
    "Hyderabad India",
    "Delhi India",

    // International Remote - US
    "Remote USA",
    "Remote United States",
    "Remote San Francisco",
    "Remote New York",

    // International Remote - Europe
    "Remote Europe",
    "Remote UK",
    "Remote Germany",
    "Remote Netherlands",

    // International Remote - Australia
    "Remote Australia",
    "Remote Sydney",

    // General Remote
    "Remote Worldwide",
    "Work from Home",
    "Remote First"
];

const JOB_TYPES = ["Full-time", "Part-time", "Freelance", "Contract"];
const EXPERIENCE = "2-4 years";

const SEARCH_CONFIGS = [
    { query: "MERN stack developer remote", tag: "MERN" },
    { query: "Node.js developer remote", tag: "Node.js" },
    { query: "Fullstack MERN developer remote", tag: "Fullstack MERN" },
    { query: "DevOps engineer remote", tag: "DevOps" },
    { query: "React developer remote", tag: "React" },
    { query: "Backend Node.js developer remote", tag: "Backend" }
];

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// Helper to get date 30 days ago in YYYY-MM-DD format
function getDateString30DaysAgo() {
    const date = new Date();
    date.setDate(date.getDate() - 30);
    return date.toISOString().split('T')[0]; // Returns YYYY-MM-DD
}

// ==================== API SCRAPERS ====================

// 1. SerpAPI - Best for Google Jobs (RECOMMENDED!)
async function scrapeSerpAPI(searchQuery, location, tag) {
    if (!SERPER_API_KEY) {
        console.log('⚠️  SerpAPI key not found. Get one free at https://serper.dev');
        return [];
    }

    try {
        console.log(`🔍 Searching SerpAPI for ${tag} in ${location}...`);

        // Query with date filter for last 30 days
        const query = `${searchQuery} ${location} after:${getDateString30DaysAgo()}`;

        const response = await axios.post(
            'https://google.serper.dev/search',
            {
                q: query,
                num: 30
            },
            {
                headers: {
                    'X-API-KEY': SERPER_API_KEY,
                    'Content-Type': 'application/json'
                }
            }
        );

        const jobs = [];

        // Parse organic results
        if (response.data.organic) {
            response.data.organic.forEach(item => {
                // Filter for job-related results
                const isJobListing =
                    item.title.toLowerCase().includes('job') ||
                    item.title.toLowerCase().includes('hiring') ||
                    item.title.toLowerCase().includes('developer') ||
                    item.title.toLowerCase().includes('engineer') ||
                    item.link.includes('linkedin.com/jobs') ||
                    item.link.includes('indeed.com') ||
                    item.link.includes('naukri.com') ||
                    item.link.includes('glassdoor.com') ||
                    item.link.includes('remoteok.com') ||
                    item.link.includes('weworkremotely.com');

                if (isJobListing && item.title) {
                    jobs.push({
                        title: item.title,
                        company: extractCompany(item.snippet || item.title),
                        location,
                        link: item.link,
                        description: item.snippet,
                        tag,
                        source: 'Serper API'
                    });
                }
            });
        }

        console.log(`✅ Found ${jobs.length} jobs from SerpAPI`);
        return jobs;
    } catch (error) {
        console.error(`❌ SerpAPI error: ${error.response?.data?.message || error.message}`);
        return [];
    }
}

// 2. Bing Web Search API
async function scrapeBingAPI(searchQuery, location, tag) {
    if (!BING_API_KEY) {
        console.log('⚠️  Bing API key not found. Get one from Azure');
        return [];
    }

    try {
        console.log(`🔍 Searching Bing API for ${tag} in ${location}...`);

        const response = await axios.get(
            'https://api.bing.microsoft.com/v7.0/search',
            {
                params: {
                    q: `${searchQuery} jobs ${location} ${EXPERIENCE}`,
                    count: 20,
                    mkt: 'en-IN'
                },
                headers: {
                    'Ocp-Apim-Subscription-Key': BING_API_KEY
                }
            }
        );

        const jobs = [];

        if (response.data.webPages?.value) {
            response.data.webPages.value.forEach(item => {
                if (item.name && (
                    item.name.toLowerCase().includes('job') ||
                    item.url.includes('linkedin.com/jobs') ||
                    item.url.includes('indeed.com') ||
                    item.url.includes('naukri.com')
                )) {
                    jobs.push({
                        title: item.name,
                        company: extractCompany(item.snippet || ''),
                        location,
                        link: item.url,
                        description: item.snippet,
                        tag,
                        source: 'Bing API'
                    });
                }
            });
        }

        console.log(`✅ Found ${jobs.length} jobs from Bing API`);
        return jobs;
    } catch (error) {
        console.error(`❌ Bing API error: ${error.message}`);
        return [];
    }
}

// 3. Naukri.com Scraper (India-specific!)
async function scrapeNaukri(browser, searchQuery, location, tag) {
    try {
        console.log(`🔍 Scraping Naukri.com for ${tag} in ${location}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = `https://www.naukri.com/${searchQuery.replace(/\s+/g, '-')}-jobs-in-${location.toLowerCase()}`;
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        await page.waitForSelector('.jobTuple, article.jobTuple', { timeout: 10000 }).catch(() => { });

        const jobs = await page.evaluate((tag) => {
            const jobCards = document.querySelectorAll('.jobTuple, article.jobTuple, .srp-jobtuple-wrapper');
            const results = [];

            jobCards.forEach(card => {
                const title = card.querySelector('.title, .jobTuple-title a')?.textContent?.trim();
                const company = card.querySelector('.companyInfo, .comp-name a')?.textContent?.trim();
                const location = card.querySelector('.location, .locWdth')?.textContent?.trim();
                const link = card.querySelector('a.title, .jobTuple-title a')?.href;

                if (title) {
                    results.push({
                        title,
                        company: company || 'N/A',
                        location: location || 'India',
                        link,
                        tag,
                        source: 'Naukri.com'
                    });
                }
            });

            return results;
        }, tag);

        await page.close();
        console.log(`✅ Found ${jobs.length} jobs from Naukri.com`);
        return jobs;
    } catch (error) {
        console.error(`❌ Naukri.com error: ${error.message}`);
        return [];
    }
}

// Helper function
function extractCompany(text) {
    const patterns = [
        /at ([A-Z][A-Za-z\s&]+)/,
        /([A-Z][A-Za-z\s&]+) is hiring/,
        /Join ([A-Z][A-Za-z\s&]+)/
    ];

    for (const pattern of patterns) {
        const match = text.match(pattern);
        if (match) return match[1].trim();
    }
    return 'N/A';
}

// ==================== MAIN SCRAPER ====================

async function scrapeUltimate() {
    console.log("🚀 ULTIMATE JOB SCRAPER - APIs + Web Scraping\n");
    console.log("=".repeat(60));

    await connectDB();

    const browser = await puppeteer.launch({
        headless: true,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });
    console.log("✅ Browser launched\n");

    let totalNewJobs = 0;
    let apiJobsCount = 0;
    let webJobsCount = 0;

    for (const location of LOCATIONS) {
        console.log(`\n🌍 Searching in: ${location}`);
        console.log("=".repeat(60));

        for (const config of SEARCH_CONFIGS) {
            console.log(`\n📍 ${config.query} - ${location}`);

            // Use APIs first (faster and more reliable)
            const [serpJobs, bingJobs, naukriJobs] = await Promise.all([
                scrapeSerpAPI(config.query, location, config.tag),
                scrapeBingAPI(config.query, location, config.tag),
                scrapeNaukri(browser, config.query, location, config.tag)
            ]);

            const allJobs = [...serpJobs, ...bingJobs, ...naukriJobs].map(job => ({
                ...job,
                searchLocation: location,
                experience: EXPERIENCE,
                jobTypes: JOB_TYPES
            }));

            if (allJobs.length > 0) {
                const stats = await saveJobs(allJobs);
                totalNewJobs += stats.savedCount;
                apiJobsCount += serpJobs.length + bingJobs.length;
                webJobsCount += naukriJobs.length;

                console.log(`💾 Saved ${stats.savedCount} new jobs`);
                console.log(`   Duplicates: ${stats.duplicateCount}`);
                console.log(`   Old jobs (>30 days): ${stats.oldJobsCount}`);
            }

            // Delay to avoid rate limiting
            await delay(2000);
        }

        await delay(3000);
    }

    await browser.close();

    console.log("\n" + "=".repeat(60));
    console.log("🎉 SCRAPING COMPLETED!");
    console.log("=".repeat(60));
    console.log(`\n📊 Summary:`);
    console.log(`  Total new jobs added: ${totalNewJobs}`);
    console.log(`  From APIs: ${apiJobsCount}`);
    console.log(`  From Web Scraping: ${webJobsCount}`);

    const totalJobs = await Job.countDocuments();
    console.log(`\n📈 Total jobs in database: ${totalJobs}`);

    // Show breakdown
    const sources = await Job.distinct('source');
    console.log(`\n📊 Jobs by source:`);
    for (const source of sources) {
        const count = await Job.countDocuments({ source });
        console.log(`  ${source}: ${count}`);
    }

    await mongoose.connection.close();
    console.log("\n✅ Done!");
}

scrapeUltimate().catch(error => {
    console.error("Fatal error:", error);
    process.exit(1);
});
