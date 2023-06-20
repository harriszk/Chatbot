const https = require('https');
const fs = require('fs');
const readline = require('readline');
require('dotenv').config({ path: '../../../../secret.env' });

// getLogs.js modified for async/awaits

const DRIVE_PATH = '../../../data/';

function makeGetRequest(options, isJSON, callback) {
  return new Promise((resolve, reject) => {
    https.get(options, (response) => {
      let result = '';
      response.on('data', (chunk) => {
        result += chunk;
      });

      response.on('end', () => {
        if (
          result.includes('No logs file') ||
          result.includes('could not load logs') ||
          result.includes('User or channel has opted out') ||
          result.includes('undefined')
        ) {
          reject();
          return;
        }

        if (isJSON) {
          callback(JSON.parse(result));
        } else {
          callback(result);
        }
        resolve();
      });
    }).on('error', (error) => {
      reject(error);
    });
  });
}

function getUserID(username) {
  return new Promise((resolve, reject) => {
    const options = {
      host: 'api.twitch.tv',
      path: `/helix/users?login=${username}`,
      headers: {
        'Client-ID': process.env.CLIENT_ID,
        Authorization: `Bearer ${process.env.ACCESS_TOKEN}`,
      },
    };

    makeGetRequest(options, true, (userData) => {
      if (userData.data[0] == undefined) {
        console.log(`${username} doesn't exist on Twitch`);
        resolve(-1);
      } else {
        resolve(userData.data[0]['id']);
      }
    }).catch((error) => {
      reject(error);
    });
  });
}

function getList(userid, channel) {
  return new Promise((resolve, reject) => {
    const path = `/list?channel=${channel}&userid=${userid}`;

    const options = {
      host: process.env.HOST,
      path: path,
      headers: {
        Accept: 'application/json',
      },
    };

    makeGetRequest(options, true, (list) => {
      resolve(list);
    }).catch((error) => {
      reject(error);
    });
  });
}

function getUsersLogs(channel, username) {
  return new Promise((resolve, reject) => {
    if (fs.existsSync(`${DRIVE_PATH}userLogs/${username}.txt`)) {
      console.log(`Have already logged ${username}`);
      resolve();
      return;
    }

    console.log(`Attempting to get ${username}'s logs`);

    getUserID(username)
      .then((userid) => {
        if (userid == -1) {
          resolve();
          return;
        }

        getList(userid, channel)
          .then((list) => {
            const promises = [];
            console.log(list)
            for (const entry in list.availableLogs) {
              const year = list.availableLogs[entry]['year'];
              const month = list.availableLogs[entry]['month'];
              const path = `/channel/${channel}/user/${username}/${year}/${month}`;

              const options = {
                host: process.env.HOST,
                path: path,
              };

              const promise = makeGetRequest(options, false, (userLogs) => {
                // This currently saves the logs out of order.
                // That may not be such a bad thing as we are going to meld
                // everyone's logs together later.

                fs.appendFile(`${DRIVE_PATH}userLogs/${username}.txt`, userLogs, (err) => {
                  if (err) throw err;
                });
              });

              promises.push(promise);
            }
            console.log(`Creating the file ${DRIVE_PATH}userLogs/${username}.txt`);

            Promise.all(promises)
              .then(() => {
                resolve();
              })
              .catch((error) => {
                reject(error);
              });
          })
          .catch((error) => {
            reject(error);
          });
      })
      .catch((error) => {
        reject(error);
      });
  });
}

async function start() {
  const file = readline.createInterface({
    input: fs.createReadStream('../../../data/allChatters.txt'),
    output: process.stdout,
    terminal: false,
  });

  for await (const username of file) {
    try {
      await getUsersLogs('mizkif', username);
    } catch (error) {
      console.error(`Error fetching logs for ${username}:`, error);
    }
  }
}

start();