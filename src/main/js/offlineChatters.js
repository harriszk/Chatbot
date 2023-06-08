const fs = require('fs');
const readline = require('readline');
const tmi = require('../../../../node_modules/tmi.js/index.js');
require('dotenv').config({ path: '../secret.env' });

chatters = [];

const opts = {
    identity: {
        username: process.env.BOT_USERNAME,
        password: process.env.OAUTH_TOKEN
    },
    channels: [
        "syn4ack"
    ]
};

// Create a client with our options
const client = new tmi.client(opts);

// Register our event handlers (defined below)
client.on('message', onMessageHandler);
client.on('connected', onConnectedHandler);

function build(){
    const file = readline.createInterface({
        input: fs.createReadStream('../../../data/offlineChatters.txt'),
        output: process.stdout,
        terminal: false
    });

    console.log("Loading users...");
    file.on('line', (line) => {
        console.log(`-> ${line}`)
        chatters.push(line);
    });
} // end build

// Connect to Twitch:
function start(){
    build();
    client.connect();  
} // end start

// Called every time a message comes in
function onMessageHandler(target, context, msg, self){
    let user = context.username;

    if(!chatters.includes(user)){
        fs.appendFile(`../../../data/offlineChatters.txt`, `${user}\n`, function (err) {
            if (err) throw err;
            console.log(`Added ${user} to list`);
        });
        chatters.push(user);
    } // end if

    return;
} // end onMessageHandler

// Called every time the bot connects to Twitch chat
function onConnectedHandler(addr, port){
	console.log(`* Connected to ${addr}:${port}`);
} // end onConnectedHandler

start();