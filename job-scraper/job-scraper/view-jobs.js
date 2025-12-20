// View all jobs in the database
import mongoose from 'mongoose';
import dotenv from 'dotenv';

dotenv.config();

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/jobbot';

async function viewJobs() {
    try {
        await mongoose.connect(MONGODB_URI);
        console.log('✅ Connected to MongoDB\n');

        const db = mongoose.connection.db;
        const collection = db.collection('jobs');

        // Get total count
        const total = await collection.countDocuments();
        console.log(`📊 Total jobs in database: ${total}\n`);

        if (total === 0) {
            console.log('⚠️  No jobs found in database. Run the scraper first!');
            await mongoose.connection.close();
            return;
        }

        // Show breakdown by tag
        console.log('📈 Jobs by Role:');
        const tags = await collection.distinct('tag');
        for (const tag of tags) {
            const count = await collection.countDocuments({ tag });
            console.log(`  ${tag}: ${count}`);
        }

        // Show breakdown by source
        console.log('\n📈 Jobs by Source:');
        const sources = await collection.distinct('source');
        for (const source of sources) {
            const count = await collection.countDocuments({ source });
            console.log(`  ${source}: ${count}`);
        }

        // Show breakdown by location
        console.log('\n📈 Jobs by Search Location:');
        const locations = await collection.distinct('searchLocation');
        for (const location of locations) {
            const count = await collection.countDocuments({ searchLocation: location });
            console.log(`  ${location}: ${count}`);
        }

        // Show sample jobs (first 5)
        console.log('\n📋 Sample Jobs (first 5):\n');
        const sampleJobs = await collection.find({}).limit(5).toArray();

        sampleJobs.forEach((job, index) => {
            console.log(`${index + 1}. ${job.title}`);
            console.log(`   Company: ${job.company}`);
            console.log(`   Location: ${job.location}`);
            console.log(`   Source: ${job.source}`);
            console.log(`   Tag: ${job.tag}`);
            console.log(`   Link: ${job.link || 'N/A'}`);
            console.log('');
        });

        // Show database and collection info
        console.log('🗄️  Database Information:');
        console.log(`   Database: ${db.databaseName}`);
        console.log(`   Collection: jobs`);
        console.log(`   Connection: ${MONGODB_URI}\n`);

        await mongoose.connection.close();
        console.log('✅ Connection closed');
    } catch (error) {
        console.error('❌ Error:', error.message);
        process.exit(1);
    }
}

viewJobs();
