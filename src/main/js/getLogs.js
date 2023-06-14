const https = require('https');
const fs = require('fs')
const { readFile } = require('fs/promises')
const readline = require('readline');
require('dotenv').config({ path: '../secret.env' });

//const DRIVE_PATH = '/Volumes/SEAGATE EXP/';
const DRIVE_PATH = '../../../data/';

async function content(path) {  
	return await readFile(path, 'utf8')
}

// Makes an https GET call and returns JSON response to callback method
// which is defined when this method is called.
function makeGetRequest(options, isJSON, callback) {
	https.get(options, (response) => {
		let result = ''
		response.on('data', function(chunk) {
			result += chunk;
		});

		response.on('end', function() {
			if(result.includes("No logs file") || result.includes("could not load logs") || result.includes("User or channel has opted out") || result.includes("undefined")) {
				return;
			}

			if(isJSON) {
				callback(JSON.parse(result));
			} else {
				callback(result);
			}
		});
	});
} // end makeCall

// Gets the userID from the Twitch API given a username.
function getUserID(username, callback) {
	var options = {
		host: 'api.twitch.tv',
		path: `/helix/users?login=${username}`,
		headers: {
			'Client-ID': process.env.CLIENT_ID,
			'Authorization': `Bearer ${process.env.ACCESS_TOKEN}`
		}
	};

	makeGetRequest(options, true, function(userData){
		console.log(userData);

		if(userData.data[0] == undefined) {
			console.log(`${username} doesn't exist on Twitch`);
			callback(-1);
		} else {
			callback(userData.data[0]['id']);
		}
	});
} // end getUserID

function getList(userid, channel, callback) {
	let path = `/list?channel=${channel}&userid=${userid}`

	let options = {
		host: process.env.HOST,
		path: path,
		headers: {
			'Accept': 'application/json'
		}
	};

	makeGetRequest(options, true, function(list) {
		callback(list)
	});
} // end getList

function getUsersLogs(channel, username) {
	// Gets user logs in channel of given year and month
	// -> /channel/{channel}/user/{username}/{year}/{month}

	// List available month and year logs of a user
	// -> /list?channel={channel}&userid={userid}

	if(fs.existsSync(`${DRIVE_PATH}userLogs/${username}.txt`)) {
		console.log(`Have already logged ${username}`);
		return;
	}

	console.log(`Attempting to get ${username}'s logs`)

	getUserID(username, function(userid) {
		if(userid == -1) {
			return;
		}
		getList(userid, channel, function(list) {
			for(entry in list.availableLogs) {
				let year = list.availableLogs[entry]['year'];
				let month = list.availableLogs[entry]['month'];
				let path = `/channel/${channel}/user/${username}/${year}/${month}`;

				console.log(`${process.env.HOST}${path}`);

				let options = {
					host: process.env.HOST,
					path: path,
				};

				makeGetRequest(options, false, function(userLogs) {
					// This currently saves the logs out of order.
					// That may not be such a bad thing as we are going to meld 
					// everyone's logs together later.

					fs.appendFile(`${DRIVE_PATH}userLogs/${username}.txt`, userLogs, function (err) {
						if (err) throw err;
					});
				});
			}
			console.log(`Creating the file ${DRIVE_PATH}userLogs/${username}.txt`);
		});
	});
} // end getUserLogs

function start() {
	const file = readline.createInterface({
        input: fs.createReadStream('../../../data/offlineChatters.txt'),
        output: process.stdout,
        terminal: false
    });

    file.on('line', (username) => {
		getUsersLogs("mizkif", username);
    });
} // end start


//getUsersLogs("lacari", "mizkif");
/*
getList(14966718, "mizkif", function(list){
	console.log(list);
});
*/

/* 
const MAX_CREATE = 10;

let chatters = [];

function sleep(seconds) {
    return new Promise((resolve) => {
    	setTimeout(resolve, seconds*1000);
    });
} // end sleep

function getChattersLogs(callback)
{ 
    for(let i = 0; i < MAX_CREATE; i++){
        if(chatters[i] !== undefined){
            getUsersLogs('mizkif', chatters[i]);
        } 
    }
    callback();
} // end getChattersLogs

function laodChatters(){
    const file = readline.createInterface({
        input: fs.createReadStream('userLogs/offline_chatters.txt'),
        output: process.stdout,
        terminal: false
    });

    file.on('line', (username) => {
		chatters.push(username);
    });
} // end laodChatters
*/