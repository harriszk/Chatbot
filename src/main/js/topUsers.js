const fs = require('fs')
const readline = require('readline');
const puppeteer = require('../../../../node_modules/puppeteer');

const url = 'https://mizkif.tigr.dev';
const N = 3000;
const interval = 250;
let chatters = [];
let i = 1;

// This script scrapes the mizkif attendance website for the users with the most watch time.

function waitFor (ms) {
    return new Promise(resolve => setTimeout(resolve, ms))
}

async function scroll (page) {
    let retryScrollCount = 3

    while (chatters.length < N) {
        try {
            let scrollPosition = await page.$eval('.tabulator-tableholder', wrapper => wrapper.scrollTop);

            await page.evaluate(() => document.querySelector('.tabulator-tableholder').scrollBy({ top: 200, behavior: 'smooth' }));
            await waitFor(200);

            await page.waitForFunction(`document.querySelector('.tabulator-tableholder').scrollTop > ${scrollPosition}`, { timeout: 1_000 });

            retryScrollCount = 3;
        } catch {
            retryScrollCount--;
        }
    }

    let file = fs.createWriteStream('../../../data/mostWatchedChatters.txt');
    file.on('error', function(err) { /* error handling */  });
    chatters.forEach(function(entry) { file.write(`${entry}\n`); });
    file.end();
}

function logData (chatter) {
    // save scraped data into file or database
    if(!chatters.includes(chatter)){
        console.log(`Added chatter: ${chatter}`);
        chatters.push(chatter);
    }
    if(chatters.length % interval == 0){
        console.log(`${interval*i} chatters logged`);
        i++;
    }
    const index = chatters.indexOf('');
    if (index > -1) { // only splice array when item is found
        chatters.splice(index, 1); // 2nd parameter means remove one item only
    }
}

function observeMutation () {

    async function onMutationHandler (mutationsList) {
        for (let mutation of mutationsList) {
            if (mutation.addedNodes.length) {
                for (let node of mutation.addedNodes) {
                    let chatter = node.querySelector('div > div:not([tabulator-field="name"]) > div').innerText
                    await logData(chatter)
                }
            }
        }
    }

    const observer = new MutationObserver(onMutationHandler)
    const virtualListNode = document.querySelector('.tabulator-tableholder')
    observer.observe(virtualListNode, { childList: true, subtree: true })
}

const scrapeChatters = async () => {
    const browser = await puppeteer.launch({
        headless: true
    });
    const page = await browser.newPage();
    await page.goto(url);

    await page.waitForSelector('div.tabulator-row.tabulator-selectable.tabulator-row-odd', {
        visible: true
    });

    await page.click('[tabulator-field="mins"]');
    await waitFor(500);
    await page.click('[tabulator-field="mins"]');

    await waitFor(5000);
    await page.exposeFunction('logData', logData);
    await page.evaluate(observeMutation);
    await scroll(page);
    
}

scrapeChatters();


/*
const chatters = await page.evaluate(() => {
    const items = document.querySelectorAll('[tabulator-field="name"]');

    const names = Array.from(items).map((name) => name.innerText);
    names.shift();

    return names;
});
console.log(chatters);

const chatters = await page.evaluate(() => {
    const items = document.querySelectorAll('[tabulator-field="name"]');

    const names = Array.from(items).map((name) => name.innerText);
    names.shift();

    return names;
});
*/