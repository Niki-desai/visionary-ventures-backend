// Multi-Source Job Scraper for MERN and Node.js Roles
import axios from "axios";
import * as cheerio from "cheerio";
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import { connectDB, Job, saveJobs } from './database.js';

// Load environment variables
dotenv.config();

// Google API Key
const GOOGLE_API_KEY = process.env.GOOGLE_API_KEY || 'AIzaSyCYh8jLWuR1NKQ2olctOdmQqe-LcmflWQU';

// Enhanced Search configurations
const LOCATIONS = ["Pune", "Remote"];
const JOB_TYPES = ["Full-time", "Part-time", "Freelance", "Contract"];
const EXPERIENCE = "2-4 years";

const SEARCH_CONFIGS = [
    {
        query: "MERN stack developer",
        tag: "MERN",
        keywords: ["MERN", "MongoDB", "Express", "React", "Node.js"]
    },
    {
        query: "Node.js developer",
        tag: "Node.js",
        keywords: ["Node.js", "NodeJS", "Backend", "JavaScript"]
    },
    {
        query: "Fullstack MERN developer",
        tag: "Fullstack MERN",
        keywords: ["Fullstack", "Full-stack", "MERN", "React", "Node.js"]
    },
    {
        query: "DevOps engineer",
        tag: "DevOps",
        keywords: ["DevOps", "CI/CD", "Docker", "Kubernetes", "AWS"]
    }
];

// Build search query with all filters
function buildSearchQuery(config, location) {
    const baseQuery = config.query;
    const filters = [
        baseQuery,
        location,
        EXPERIENCE,
        JOB_TYPES.join(" OR ")
    ].filter(Boolean).join(" ");

    return filters;
}

// Common headers to mimic a real browser
const HEADERS = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
    "Accept-Language": "en-US,en;q=0.9",
    "Accept-Encoding": "gzip, deflate, br",
    "Connection": "keep-alive",
};

// ==================== SCRAPER FUNCTIONS ====================

// 1. RemoteOK Scraper
async function scrapeRemoteOK(searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping RemoteOK for ${tag}...`);

        const url = `https://remoteok.com/remote-${encodeURIComponent(searchQuery.toLowerCase().replace(/\s+/g, '-'))}-jobs`;
        const { data } = await axios.get(url, { headers: HEADERS });
        const $ = cheerio.load(data);
        const jobs = [];

        $("tr.job").each((_, el) => {
            const title = $(el).find("h2[itemprop='title']").text().trim();
            const company = $(el).find("h3[itemprop='name']").text().trim();
            const location = $(el).find(".location").text().trim() || "Remote";
            const link = $(el).find("a.preventLink").attr("href");

            if (title) {
                jobs.push({
                    title,
                    company: company || "N/A",
                    location,
                    link: link ? `https://remoteok.com${link}` : null,
                    tag,
                    source: "RemoteOK"
                });
            }
        });

        console.log(`✅ Found ${jobs.length} jobs from RemoteOK`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping RemoteOK: ${error.message}`);
        return [];
    }
}

// 2. WeWorkRemotely Scraper
async function scrapeWeWorkRemotely(searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping WeWorkRemotely for ${tag}...`);

        const url = "https://weworkremotely.com/categories/remote-programming-jobs";
        const { data } = await axios.get(url, { headers: HEADERS });
        const $ = cheerio.load(data);
        const jobs = [];

        $("li.feature").each((_, el) => {
            const title = $(el).find(".title").text().trim();
            const company = $(el).find(".company").text().trim();
            const location = $(el).find(".region").text().trim() || "Remote";
            const link = $(el).find("a").attr("href");

            // Filter by search query
            if (title && (title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                title.toLowerCase().includes('node') ||
                title.toLowerCase().includes('mern'))) {
                jobs.push({
                    title,
                    company: company || "N/A",
                    location,
                    link: link ? `https://weworkremotely.com${link}` : null,
                    tag,
                    source: "WeWorkRemotely"
                });
            }
        });

        console.log(`✅ Found ${jobs.length} jobs from WeWorkRemotely`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping WeWorkRemotely: ${error.message}`);
        return [];
    }
}

// 3. Indeed Scraper
async function scrapeIndeed(searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping Indeed for ${tag}...`);

        const url = `https://www.indeed.com/jobs?q=${encodeURIComponent(searchQuery)}&l=${encodeURIComponent(LOCATION)}`;
        const { data } = await axios.get(url, { headers: HEADERS });
        const $ = cheerio.load(data);
        const jobs = [];

        $("div.job_seen_beacon, div.jobsearch-SerpJobCard").each((_, el) => {
            const title = $(el).find("h2.jobTitle, a.jcs-JobTitle").text().trim();
            const company = $(el).find("span.companyName").text().trim();
            const location = $(el).find("div.companyLocation").text().trim();
            const link = $(el).find("a.jcs-JobTitle").attr("href");

            if (title) {
                jobs.push({
                    title,
                    company: company || "N/A",
                    location: location || "Remote",
                    link: link ? `https://www.indeed.com${link}` : null,
                    tag,
                    source: "Indeed"
                });
            }
        });

        console.log(`✅ Found ${jobs.length} jobs from Indeed`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping Indeed: ${error.message}`);
        return [];
    }
}

// 4. Google Jobs Scraper (with API Key)
async function scrapeGoogleJobs(searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping Google Jobs for ${tag}...`);

        const url = `https://www.google.com/search?q=${encodeURIComponent(searchQuery + " jobs " + LOCATION)}&ibp=htl;jobs`;

        // Enhanced headers with API key
        const enhancedHeaders = {
            ...HEADERS,
            'X-Goog-Api-Key': GOOGLE_API_KEY,
            'Referer': 'https://www.google.com/'
        };

        const { data } = await axios.get(url, { headers: enhancedHeaders });
        const $ = cheerio.load(data);
        const jobs = [];

        $("div.PwjeAc, li.iFjolb").each((_, el) => {
            const title = $(el).find("div.BjJfJf, h2").text().trim();
            const company = $(el).find("div.vNEEBe, span.uMdZh").text().trim();
            const location = $(el).find("div.Qk80Jf, span.RP0xob").text().trim();
            const link = $(el).find("a").attr("href");

            if (title) {
                jobs.push({
                    title,
                    company: company || "N/A",
                    location: location || "Remote",
                    link: link ? `https://www.google.com${link}` : null,
                    tag,
                    source: "Google Jobs"
                });
            }
        });

        console.log(`✅ Found ${jobs.length} jobs from Google Jobs`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping Google Jobs: ${error.message}`);
        return [];
    }
}

// 5. Bing Jobs Scraper
async function scrapeBingJobs(searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping Bing Jobs for ${tag}...`);

        const url = `https://www.bing.com/jobs?q=${encodeURIComponent(searchQuery)}&l=${encodeURIComponent(LOCATION)}`;
        const { data } = await axios.get(url, { headers: HEADERS });
        const $ = cheerio.load(data);
        const jobs = [];

        $("li.job-item, div.job_card").each((_, el) => {
            const title = $(el).find("h2, a.jobtitle").text().trim();
            const company = $(el).find("div.company, span.company").text().trim();
            const location = $(el).find("div.location, span.location").text().trim();
            const link = $(el).find("a").attr("href");

            if (title) {
                jobs.push({
                    title,
                    company: company || "N/A",
                    location: location || "Remote",
                    link: link || null,
                    tag,
                    source: "Bing Jobs"
                });
            }
        });

        console.log(`✅ Found ${jobs.length} jobs from Bing Jobs`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping Bing Jobs: ${error.message}`);
        return [];
    }
}

// ==================== MAIN SCRAPER ====================

async function scrapeAllJobs() {
    console.log("🚀 Starting Multi-Source Job Scraper...\n");
    console.log("=".repeat(60));

    // Connect to database
    await connectDB();

    for (const location of LOCATIONS) {
        console.log(`\n🌍 Searching in: ${location}`);
        console.log("=".repeat(60));

        for (const config of SEARCH_CONFIGS) {
            const searchQuery = buildSearchQuery(config, location);

            console.log(`\n📍 Searching for: "${config.query}" in ${location} (Tag: ${config.tag})`);
            console.log(`🔍 Full query: "${searchQuery}"`);
            console.log("=".repeat(60));

            // Scrape all sources concurrently
            const [remoteOKJobs, weWorkJobs, indeedJobs, googleJobs, bingJobs] = await Promise.all([
                scrapeRemoteOK(searchQuery, config.tag),
                scrapeWeWorkRemotely(searchQuery, config.tag),
                scrapeIndeed(searchQuery, config.tag),
                scrapeGoogleJobs(searchQuery, config.tag),
                scrapeBingJobs(searchQuery, config.tag)
            ]);

            // Combine all jobs and add location info
            const allJobs = [...remoteOKJobs, ...weWorkJobs, ...indeedJobs, ...googleJobs, ...bingJobs].map(job => ({
                ...job,
                location: job.location || location,
                searchLocation: location,
                experience: EXPERIENCE,
                jobTypes: JOB_TYPES
            }));

            // Save to single collection
            console.log("\n💾 Saving jobs to database...\n");
            const stats = await saveJobs(allJobs);

            console.log(`✅ Total new jobs saved for ${config.tag} in ${location}: ${stats.savedCount}`);
            console.log(`⚠️  Duplicates skipped: ${stats.duplicateCount}`);

            // Show breakdown by source
            const breakdown = allJobs.reduce((acc, job) => {
                acc[job.source] = (acc[job.source] || 0) + 1;
                return acc;
            }, {});

            console.log("\n📊 Jobs found by source:");
            Object.entries(breakdown).forEach(([source, count]) => {
                console.log(`  ${source}: ${count}`);
            });
        }
    }

    console.log("\n" + "=".repeat(60));
    console.log("🎉 Scraping completed successfully!");
    console.log("=".repeat(60));

    // Show total stats from database
    const totalJobs = await Job.countDocuments();
    const mernJobs = await Job.countDocuments({ tag: 'MERN' });
    const nodeJobs = await Job.countDocuments({ tag: 'Node.js' });

    console.log(`\n📈 Database Statistics:`);
    console.log(`  Total jobs in database: ${totalJobs}`);
    console.log(`  MERN jobs: ${mernJobs}`);
    console.log(`  Node.js jobs: ${nodeJobs}`);

    // Close database connection
    await mongoose.connection.close();
    console.log("\n✅ Database connection closed");
}

// Run the scraper
scrapeAllJobs().catch(error => {
    console.error("Fatal error:", error);
    process.exit(1);
});