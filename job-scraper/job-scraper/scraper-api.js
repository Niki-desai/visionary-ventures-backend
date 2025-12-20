// Google API-based Job Scraper
// Uses Google Custom Search API for better reliability
import axios from 'axios';
import dotenv from 'dotenv';
import mongoose from 'mongoose';
import { connectDB, Job, saveJobs } from './database.js';

dotenv.config();

const GOOGLE_API_KEY = process.env.GOOGLE_API_KEY;
const GOOGLE_SEARCH_ENGINE_ID = process.env.GOOGLE_SEARCH_ENGINE_ID || 'YOUR_SEARCH_ENGINE_ID'; // You'll need to create this

// Search configurations
const SEARCH_CONFIGS = [
    { query: "MERN stack developer jobs remote", tag: "MERN" },
    { query: "Node.js developer jobs remote", tag: "Node.js" }
];

// ==================== GOOGLE CUSTOM SEARCH API ====================

async function searchGoogleJobs(searchQuery, tag) {
    try {
        console.log(`\n🔍 Searching Google for ${tag}...`);

        if (!GOOGLE_API_KEY) {
            console.error('❌ Google API key not found in .env file');
            return [];
        }

        // Google Custom Search API endpoint
        const url = 'https://www.googleapis.com/customsearch/v1';

        const params = {
            key: GOOGLE_API_KEY,
            cx: GOOGLE_SEARCH_ENGINE_ID,
            q: searchQuery,
            num: 10 // Number of results (max 10 per request)
        };

        const response = await axios.get(url, { params });
        const jobs = [];

        if (response.data.items) {
            response.data.items.forEach(item => {
                // Extract job information from search results
                jobs.push({
                    title: item.title,
                    company: extractCompany(item.snippet),
                    location: 'Remote',
                    link: item.link,
                    description: item.snippet,
                    tag,
                    source: 'Google Search API'
                });
            });
        }

        console.log(`✅ Found ${jobs.length} jobs from Google Search API`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error searching Google: ${error.message}`);
        if (error.response) {
            console.error(`Status: ${error.response.status}`);
            console.error(`Error: ${error.response.data.error?.message || 'Unknown error'}`);
        }
        return [];
    }
}

// Helper function to extract company name from snippet
function extractCompany(snippet) {
    // Try to extract company name from common patterns
    const patterns = [
        /at ([A-Z][A-Za-z\s&]+)/,
        /([A-Z][A-Za-z\s&]+) is hiring/,
        /Join ([A-Z][A-Za-z\s&]+)/
    ];

    for (const pattern of patterns) {
        const match = snippet.match(pattern);
        if (match) {
            return match[1].trim();
        }
    }

    return 'N/A';
}

// ==================== SERPER API (Alternative) ====================
// Serper.dev provides a Google Search API that's easier to use for job searches

async function searchSerperJobs(searchQuery, tag) {
    try {
        console.log(`\n🔍 Searching via Serper API for ${tag}...`);

        const SERPER_API_KEY = process.env.SERPER_API_KEY || "eb419d5d1ffffc208400c40fc78d0fdccdc727db";

        if (!SERPER_API_KEY) {
            console.log('⚠️  Serper API key not found, skipping...');
            return [];
        }

        const response = await axios.post(
            'https://google.serper.dev/search',
            {
                q: searchQuery,
                num: 10
            },
            {
                headers: {
                    'X-API-KEY': SERPER_API_KEY || "eb419d5d1ffffc208400c40fc78d0fdccdc727db",
                    'Content-Type': 'application/json'
                }
            }
        );

        const jobs = [];

        if (response.data.organic) {
            response.data.organic.forEach(item => {
                jobs.push({
                    title: item.title,
                    company: extractCompany(item.snippet || ''),
                    location: 'Remote',
                    link: item.link,
                    description: item.snippet,
                    tag,
                    source: 'Serper API'
                });
            });
        }

        console.log(`✅ Found ${jobs.length} jobs from Serper API`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error with Serper API: ${error.message}`);
        return [];
    }
}

// ==================== JOB BOARD SPECIFIC APIs ====================

// Indeed API (requires publisher ID)
async function searchIndeedAPI(searchQuery, tag) {
    try {
        console.log(`\n🔍 Searching Indeed API for ${tag}...`);

        const INDEED_PUBLISHER_ID = process.env.INDEED_PUBLISHER_ID;

        if (!INDEED_PUBLISHER_ID) {
            console.log('⚠️  Indeed Publisher ID not found, skipping...');
            return [];
        }

        const url = 'http://api.indeed.com/ads/apisearch';
        const params = {
            publisher: INDEED_PUBLISHER_ID,
            q: searchQuery,
            l: 'Remote',
            format: 'json',
            v: '2',
            limit: 25
        };

        const response = await axios.get(url, { params });
        const jobs = [];

        if (response.data.results) {
            response.data.results.forEach(job => {
                jobs.push({
                    title: job.jobtitle,
                    company: job.company,
                    location: job.formattedLocation,
                    link: job.url,
                    description: job.snippet,
                    tag,
                    source: 'Indeed API'
                });
            });
        }

        console.log(`✅ Found ${jobs.length} jobs from Indeed API`);
        return jobs;
    } catch (error) {
        console.error(`❌ Error with Indeed API: ${error.message}`);
        return [];
    }
}

// ==================== MAIN SCRAPER ====================

async function scrapeAllJobs() {
    console.log("🚀 Starting API-based Job Scraper...\n");
    console.log("=".repeat(60));

    // Connect to database
    await connectDB();

    for (const config of SEARCH_CONFIGS) {
        console.log(`\n📍 Searching for: "${config.query}" (Tag: ${config.tag})`);
        console.log("=".repeat(60));

        // Search using available APIs
        const [googleJobs, serperJobs, indeedJobs] = await Promise.all([
            searchGoogleJobs(config.query, config.tag),
            searchSerperJobs(config.query, config.tag),
            searchIndeedAPI(config.query, config.tag)
        ]);

        // Combine all jobs
        const allJobs = [...googleJobs, ...serperJobs, ...indeedJobs];

        if (allJobs.length === 0) {
            console.log('\n⚠️  No jobs found. Make sure your API keys are configured correctly.');
            console.log('📝 Required environment variables:');
            console.log('   - GOOGLE_API_KEY (provided)');
            console.log('   - GOOGLE_SEARCH_ENGINE_ID (needs to be created)');
            console.log('   - SERPER_API_KEY (optional, from serper.dev)');
            console.log('   - INDEED_PUBLISHER_ID (optional, from indeed.com/publisher)');
            continue;
        }

        // Save to database
        console.log("\n💾 Saving jobs to database...\n");
        const stats = await saveJobs(allJobs);

        console.log(`✅ Total new jobs saved for ${config.tag}: ${stats.savedCount}`);
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
