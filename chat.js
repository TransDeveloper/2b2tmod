const ws = require('ws')
const wss = new ws.Server({ port: 80 });

const debug = false;

let clients = 0;

wss.on('connection', function connection(ws) {
	ws.on('message', function message(data) {
		if(debug){console.log(data.toString());}
		wss.clients.forEach(function(client) {
			client.send(data);
		});
	});
	clients++;
});

wss.on('close', function() {
	clients--;
});

setInterval(function(){
	console.log(`${clients} client(/s) connected`);
}, 1000);
