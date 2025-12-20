// Export all jobs to JSON file
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import fs from 'fs';

dotenv.config();

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/jobbot';

async function exportJobs() {
    try {
        await mongoose.connect(MONGODB_URI);
        console.log('✅ Connected to MongoDB\n');

        const db = mongoose.connection.db;
        const collection = db.collection('jobs');

        // Get all jobs
        const jobs = await collection.find({}).toArray();

        console.log(`📊 Found ${jobs.length} jobs\n`);

        // Export to JSON file
        const outputFile = 'jobs-export.json';
        fs.writeFileSync(outputFile, JSON.stringify(jobs, null, 2));

        console.log(`✅ Exported ${jobs.length} jobs to ${outputFile}\n`);

        // Also create a CSV export
        const csvFile = 'jobs-export.csv';
        const csvHeader = 'Title,Company,Location,Source,Tag,Experience,Link\n';
        const csvRows = jobs.map(job =>
            `"${job.title}","${job.company}","${job.location}","${job.source}","${job.tag}","${job.experience || ''}","${job.link || ''}"`
        ).join('\n');

        fs.writeFileSync(csvFile, csvHeader + csvRows);
        console.log(`✅ Exported ${jobs.length} jobs to ${csvFile}\n`);

        await mongoose.connection.close();
        console.log('✅ Connection closed');
        console.log(`\n📁 Files created in: ${process.cwd()}`);
    } catch (error) {
        console.error('❌ Error:', error.message);
        process.exit(1);
    }
}

exportJobs();
