// puppeteerScrape.js
import puppeteer from "puppeteer";

async function scrapeJobs() {
    const browser = await puppeteer.launch({
        headless: true,
        args: ["--no-sandbox", "--disable-setuid-sandbox"],
    });

    const page = await browser.newPage();

    await page.setUserAgent(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
    );

    await page.goto("https://example.com/jobs", {
        waitUntil: "networkidle2",
    });

    const jobs = await page.evaluate(() => {
        return Array.from(document.querySelectorAll(".job-card")).map(el => ({
            title: el.querySelector(".job-title")?.innerText,
            location: el.querySelector(".location")?.innerText,
            link: el.querySelector("a")?.href,
        }));
    });

    console.log(jobs);

    await browser.close();
}

scrapeJobs();
