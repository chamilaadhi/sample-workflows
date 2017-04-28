var http = require("http");
var url = require('url');
var server_url =  "/testserver";

// create a Oauth APP in API Manager and add the keys here
var clientId = "xxxxxxxxxxxxx";
var clientSecret = "xxxxxxxxxxxxxxx";

var server = http.createServer(function(request, response) {
  	var requestURL = url.parse(request.url,true);
	var pathname = url.parse(request.url,true).pathname;

var query = url.parse(request.url,true).query;
var authHeader = new Buffer(clientId + ":" + clientSecret).toString('base64');
var curl = "curl -k -X POST -H \"Authorization: Bearer " +authHeader+ "\" -H \"Content-Type: application/x-www-form-urlencoded\"  -d  'grant_type=password&username=Smith&password=Smith&validity_period=3600&scope=apim:api_workflow' \"https://localhost:9292/keyserver/oauth2/token\""


var approveCurl = 

"curl -k -X PUT \  'https://localhost:9292/api/am/store/v1.0/workflows/"+ query.externalRef + "'" +
 " -H 'authorization: Bearer {token}' "+
 " -H 'content-type: application/json' "+
 " -d '{\"status\" : \"APPROVED\",\"attributes\" : {}}' ";



	var body = 
		"<!DOCTYPE html>" +
		"<html>" +
		"<body>" +

		"<h1>This is a sample page to handle redirection call</h1>" +

		"<p><b>ExternalRef</b>: " + query.externalRef +"</p>" +
		"<p><b>Token generation curl</b> : " + curl +"</p>"+
		"<p><b>Approve curl</b> : " + approveCurl +"</p>"+

		"</body>" +
		"</html>" 
		;
	request.on('data', function (chunk) {
	    console.log('BODY: ' + chunk);
	});
        
        response.writeHead(200, {
				"Content-Type": "text/html",
				"Content-Length": body.length,
				});  
 	response.write(body);
  	response.end();

	
 
});
 
server.listen(7777);
console.log("Server is listening");
