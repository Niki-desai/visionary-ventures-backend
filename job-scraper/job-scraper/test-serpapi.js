// Quick test of SerpAPI
import axios from 'axios';
import dotenv from 'dotenv';

dotenv.config();

const SERPER_API_KEY = process.env.SERPER_API_KEY || "eb419d5d1ffffc208400c40fc78d0fdccdc727db";

async function testSerpAPI() {
    try {
        console.log('🔍 Testing SerpAPI...');
        console.log(`API Key: ${SERPER_API_KEY.substring(0, 10)}...`);

        const response = await axios.post(
            'https://google.serper.dev/search',
            {
                q: "MERN stack developer jobs remote",
                num: 10
            },
            {
                headers: {
                    'X-API-KEY': SERPER_API_KEY,
                    'Content-Type': 'application/json'
                }
            }
        );

        console.log('\n✅ SerpAPI is working!');
        console.log(`Found ${response.data.organic?.length || 0} results`);

        if (response.data.organic && response.data.organic.length > 0) {
            console.log('\n📋 Sample results:');
            response.data.organic.slice(0, 3).forEach((item, i) => {
                console.log(`\n${i + 1}. ${item.title}`);
                console.log(`   Link: ${item.link}`);
            });
        }

    } catch (error) {
        console.error('\n❌ SerpAPI Error:');
        console.error(`Status: ${error.response?.status}`);
        console.error(`Message: ${error.response?.data?.message || error.message}`);
        console.error(`\nFull error:`, error.response?.data);
    }
}

testSerpAPI();
