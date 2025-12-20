// Quick script to fix database source field issues
import mongoose from 'mongoose';
import dotenv from 'dotenv';

dotenv.config();

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/jobbot';

async function fixDatabase() {
    try {
        await mongoose.connect(MONGODB_URI);
        console.log('✅ Connected to MongoDB');

        const db = mongoose.connection.db;
        const collection = db.collection('jobs');

        // Find all documents with invalid source field
        const invalidDocs = await collection.find({
            source: { $type: 'object' }
        }).toArray();

        console.log(`\n🔍 Found ${invalidDocs.length} documents with invalid source field`);

        if (invalidDocs.length > 0) {
            // Delete invalid documents
            const result = await collection.deleteMany({
                source: { $type: 'object' }
            });
            console.log(`✅ Deleted ${result.deletedCount} invalid documents`);
        }

        // Also delete documents with null source
        const nullResult = await collection.deleteMany({
            source: null
        });
        console.log(`✅ Deleted ${nullResult.deletedCount} documents with null source`);

        // Show final stats
        const total = await collection.countDocuments();
        console.log(`\n📊 Total jobs in database: ${total}`);

        // Show breakdown by source
        const sources = await collection.distinct('source');
        console.log('\n📈 Jobs by source:');
        for (const source of sources) {
            const count = await collection.countDocuments({ source });
            console.log(`  ${source}: ${count}`);
        }

        await mongoose.connection.close();
        console.log('\n✅ Database fixed and connection closed');
    } catch (error) {
        console.error('❌ Error:', error.message);
        process.exit(1);
    }
}

fixDatabase();
