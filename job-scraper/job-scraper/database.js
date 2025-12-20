import mongoose from 'mongoose';
import dotenv from 'dotenv';

dotenv.config();

const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/jobbot';

// Connect to MongoDB
export async function connectDB() {
    try {
        await mongoose.connect(MONGODB_URI);
        console.log('✅ Connected to MongoDB successfully');
    } catch (error) {
        console.error('❌ MongoDB connection error:', error.message);
        process.exit(1);
    }
}

// Single Job Schema for all sources
const jobSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true
    },
    company: {
        type: String,
        default: 'N/A'
    },
    location: {
        type: String,
        default: 'Remote'
    },
    searchLocation: {
        type: String,
        default: 'Remote'
    },
    link: {
        type: String
    },
    description: {
        type: String
    },
    source: {
        type: String,
        required: true,
        enum: ['RemoteOK', 'WeWorkRemotely', 'Indeed', 'Google Jobs', 'Bing Jobs', 'LinkedIn', 'Glassdoor', 'Naukri.com', 'Google (SerpAPI)', 'Bing API', 'Serper API']
    },
    tag: {
        type: String,
        enum: ['MERN', 'Node.js', 'Fullstack MERN', 'DevOps', 'React', 'Backend'],
        required: true
    },
    experience: {
        type: String,
        default: '2-4 years'
    },
    jobTypes: {
        type: [String],
        default: ['Full-time']
    },
    postedDate: {
        type: Date,
        default: Date.now
    },
    scrapedAt: {
        type: Date,
        default: Date.now
    }
}, {
    timestamps: true // Adds createdAt and updatedAt automatically
});

// Create indexes for better query performance
jobSchema.index({ source: 1, tag: 1 });
jobSchema.index({ title: 1, company: 1, source: 1 }, { unique: true });
jobSchema.index({ scrapedAt: -1 });
jobSchema.index({ postedDate: -1 });

const Job = mongoose.model('Job', jobSchema);

// Helper function to save jobs with duplicate detection and date filtering
export async function saveJobs(jobs) {
    let savedCount = 0;
    let duplicateCount = 0;
    let oldJobsCount = 0;

    // Calculate date 30 days ago
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

    for (const jobData of jobs) {
        try {
            // Skip if job is older than 30 days (if we can determine the date)
            if (jobData.postedDate && new Date(jobData.postedDate) < thirtyDaysAgo) {
                oldJobsCount++;
                continue;
            }

            // Check for duplicate based on title, company, and source
            const existingJob = await Job.findOne({
                title: jobData.title,
                company: jobData.company,
                source: jobData.source
            });

            if (existingJob) {
                duplicateCount++;
                continue;
            }

            // Save new job
            const job = new Job({
                ...jobData,
                postedDate: jobData.postedDate || new Date() // Default to now if not provided
            });
            await job.save();
            savedCount++;
        } catch (error) {
            console.error(`Error saving job: ${error.message}`);
        }
    }

    return {
        savedCount,
        duplicateCount,
        oldJobsCount,
        total: jobs.length
    };
}
