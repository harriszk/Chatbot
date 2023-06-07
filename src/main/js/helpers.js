const https = require('https');

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