const https = require('https');
const fs = require('fs')
const { readFile } = require('fs/promises')
const readline = require('readline');

module.exports = {
	makeGetRequest: function(options, callback){
		https.get(options, (response) => {
			let result = ''
			response.on('data', function (chunk) {
				result += chunk;
			});
	
			response.on('end', function () {
				callback(result);
			});
		});
	} // end makeCall
};