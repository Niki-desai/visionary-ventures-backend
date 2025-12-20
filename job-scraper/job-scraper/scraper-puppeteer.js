// Enhanced Job Scraper using Puppeteer for better success rate
import puppeteer from 'puppeteer';
import mongoose from 'mongoose';
import { connectDB, Job, saveJobs } from './database.js';

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

// ==================== PUPPETEER SCRAPERS ====================

// 1. LinkedIn Jobs Scraper (More reliable)
async function scrapeLinkedIn(browser, searchQuery, tag, location) {
    try {
        console.log(`\n🔍 Scraping LinkedIn for ${tag}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = `https://www.linkedin.com/jobs/search?keywords=${encodeURIComponent(searchQuery)}&location=${encodeURIComponent(location)}&f_WT=2`;
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        const jobs = await page.evaluate((tag) => {
            const jobCards = document.querySelectorAll('.base-card');
            const results = [];

            jobCards.forEach(card => {
                const title = card.querySelector('.base-search-card__title')?.textContent?.trim();
                const company = card.querySelector('.base-search-card__subtitle')?.textContent?.trim();
                const location = card.querySelector('.job-search-card__location')?.textContent?.trim();
                const link = card.querySelector('a')?.href;

                if (title) {
                    results.push({
                        title,
                        company: company || 'N/A',
                        location: location || 'Remote',
                        link,
                        tag,
                        source: 'LinkedIn'
                    });
                }
            });

            return results;
        }, tag);

        await page.close();
        console.log(`✅ Found ${jobs.length} jobs from LinkedIn`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping LinkedIn: ${error.message}`);
        return [];
    }
}

// 2. Indeed Scraper with Puppeteer
async function scrapeIndeed(browser, searchQuery, tag, location) {
    try {
        console.log(`\n🔍 Scraping Indeed for ${tag}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = `https://www.indeed.com/jobs?q=${encodeURIComponent(searchQuery)}&l=${encodeURIComponent(location)}&remotejob=032b3046-06a3-4876-8dfd-474eb5e7ed11`;
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        const jobs = await page.evaluate((tag) => {
            const jobCards = document.querySelectorAll('.job_seen_beacon, .jobsearch-SerpJobCard, div[data-jk]');
            const results = [];

            jobCards.forEach(card => {
                const title = card.querySelector('h2.jobTitle, a.jcs-JobTitle span')?.textContent?.trim();
                const company = card.querySelector('.companyName')?.textContent?.trim();
                const location = card.querySelector('.companyLocation')?.textContent?.trim();
                const link = card.querySelector('a.jcs-JobTitle')?.href || card.querySelector('h2.jobTitle a')?.href;

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
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping Indeed: ${error.message}`);
        return [];
    }
}

// 3. RemoteOK Scraper with Puppeteer
async function scrapeRemoteOK(browser, searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping RemoteOK for ${tag}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = `https://remoteok.com/remote-${searchQuery.toLowerCase().replace(/\s+/g, '-')}-jobs`;
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        const jobs = await page.evaluate((tag) => {
            const jobRows = document.querySelectorAll('tr.job');
            const results = [];

            jobRows.forEach(row => {
                const title = row.querySelector('h2[itemprop="title"]')?.textContent?.trim();
                const company = row.querySelector('h3[itemprop="name"]')?.textContent?.trim();
                const location = row.querySelector('.location')?.textContent?.trim();
                const link = row.querySelector('a.preventLink')?.href;

                if (title) {
                    results.push({
                        title,
                        company: company || 'N/A',
                        location: location || 'Remote',
                        link: link ? `https://remoteok.com${link}` : null,
                        tag,
                        source: 'RemoteOK'
                    });
                }
            });

            return results;
        }, tag);

        await page.close();
        console.log(`✅ Found ${jobs.length} jobs from RemoteOK`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping RemoteOK: ${error.message}`);
        return [];
    }
}

// 4. Glassdoor Scraper
async function scrapeGlassdoor(browser, searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping Glassdoor for ${tag}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = `https://www.glassdoor.com/Job/jobs.htm?sc.keyword=${encodeURIComponent(searchQuery)}&locT=C&locId=1147401`;
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        const jobs = await page.evaluate((tag) => {
            const jobCards = document.querySelectorAll('li[data-test="jobListing"], .JobsList_jobListItem__wjTHv');
            const results = [];

            jobCards.forEach(card => {
                const title = card.querySelector('a[data-test="job-link"]')?.textContent?.trim();
                const company = card.querySelector('[data-test="employer-name"]')?.textContent?.trim();
                const location = card.querySelector('[data-test="emp-location"]')?.textContent?.trim();
                const link = card.querySelector('a[data-test="job-link"]')?.href;

                if (title) {
                    results.push({
                        title,
                        company: company || 'N/A',
                        location: location || 'Remote',
                        link,
                        tag,
                        source: 'Glassdoor'
                    });
                }
            });

            return results;
        }, tag);

        await page.close();
        console.log(`✅ Found ${jobs.length} jobs from Glassdoor`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping Glassdoor: ${error.message}`);
        return [];
    }
}

// 5. WeWorkRemotely Scraper
async function scrapeWeWorkRemotely(browser, searchQuery, tag) {
    try {
        console.log(`\n🔍 Scraping WeWorkRemotely for ${tag}...`);

        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36');

        const url = 'https://weworkremotely.com/categories/remote-programming-jobs';
        await page.goto(url, { waitUntil: 'networkidle2', timeout: 30000 });

        const jobs = await page.evaluate((searchQuery, tag) => {
            const jobListings = document.querySelectorAll('li.feature');
            const results = [];

            jobListings.forEach(listing => {
                const title = listing.querySelector('.title')?.textContent?.trim();
                const company = listing.querySelector('.company')?.textContent?.trim();
                const location = listing.querySelector('.region')?.textContent?.trim();
                const link = listing.querySelector('a')?.href;

                // Filter by search query
                if (title && (title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                    title.toLowerCase().includes('node') ||
                    title.toLowerCase().includes('mern') ||
                    title.toLowerCase().includes('javascript'))) {
                    results.push({
                        title,
                        company: company || 'N/A',
                        location: location || 'Remote',
                        link: link ? `https://weworkremotely.com${link}` : null,
                        tag,
                        source: 'WeWorkRemotely'
                    });
                }
            });

            return results;
        }, searchQuery, tag);

        await page.close();
        console.log(`✅ Found ${jobs.length} jobs from WeWorkRemotely`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error scraping WeWorkRemotely: ${error.message}`);
        return [];
    }
}

// ==================== MAIN SCRAPER ====================

async function scrapeAllJobs() {
    console.log("🚀 Starting Enhanced Multi-Source Job Scraper with Puppeteer...\n");
    console.log("=".repeat(60));

    // Connect to database
    await connectDB();

    // Launch browser
    console.log("\n🌐 Launching browser...");
    const browser = await puppeteer.launch({
        headless: true,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });
    console.log("✅ Browser launched\n");

    for (const location of LOCATIONS) {
        console.log(`\n🌍 Searching in: ${location}`);
        console.log("=".repeat(60));

        for (const config of SEARCH_CONFIGS) {
            const searchQuery = buildSearchQuery(config, location);

            console.log(`\n📍 Searching for: "${config.query}" in ${location} (Tag: ${config.tag})`);
            console.log(`🔍 Full query: "${searchQuery}"`);
            console.log("=".repeat(60));

            // Scrape all sources sequentially to avoid overwhelming the browser
            const linkedInJobs = await scrapeLinkedIn(browser, searchQuery, config.tag, location);
            const indeedJobs = await scrapeIndeed(browser, searchQuery, config.tag, location);
            const remoteOKJobs = await scrapeRemoteOK(browser, searchQuery, config.tag);
            const glassdoorJobs = await scrapeGlassdoor(browser, searchQuery, config.tag);
            const weWorkJobs = await scrapeWeWorkRemotely(browser, searchQuery, config.tag);

            // Combine all jobs and add location info
            const allJobs = [...linkedInJobs, ...indeedJobs, ...remoteOKJobs, ...glassdoorJobs, ...weWorkJobs].map(job => ({
                ...job,
                location: job.location || location,
                searchLocation: location,
                experience: EXPERIENCE,
                jobTypes: JOB_TYPES
            }));

            // Save to database
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

    // Close browser
    await browser.close();
    console.log("\n🌐 Browser closed");

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

    // Show jobs by source
    const sources = await Job.distinct('source');
    console.log(`\n📊 Jobs by source:`);
    for (const source of sources) {
        const count = await Job.countDocuments({ source });
        console.log(`  ${source}: ${count}`);
    }

    // Close database connection
    await mongoose.connection.close();
    console.log("\n✅ Database connection closed");
}

// Run the scraper
scrapeAllJobs().catch(error => {
    console.error("Fatal error:", error);
    process.exit(1);
});
