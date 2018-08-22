var client = document.getElementById("client");

var echoText = document.getElementById("echoText");
echoText.value = "";
var message = document.getElementById("message");

var webSocket;

function wsConnect()
{
	webSocket = new WebSocket("ws://192.168.0.103:8080/chat/websocketendpoint/" + client.value);
	//event handlers
	webSocket.onopen = function(message){ wsOpen(message);};
	webSocket.onmessage = function(message){ wsGetMessage(message);};
	webSocket.onclose = function(message){ wsClose(message);};
	webSocket.onerror = function(message){ wsError(message);};
}

function wsOpen(message)
{
    echoText.value += client.value + " Connected ... \n";
}

function wsSendMessage()
{	
	var msg = client.value + ":" + users.value + ":" + message.value;
    webSocket.send(msg);
    echoText.value += client.value + ": " + message.value + "\n";
    message.value = "";
}
function wsCloseConnection()
{
    webSocket.close();
}
function wsGetMessage(message)
{	
	if (message.data.startsWith("SERVER") && message.data.includes("ONLINE") && !message.data.includes("No-user is ONLINE"))
	{
		//if online message, then add user to the users select.
		var lastIndex = message.data.indexOf(" ");
		var user = message.data.substring(8, lastIndex);
		var select = document.getElementById("users");
		select.options[select.options.length] = new Option(user, user);
	}
	
	var msg = message.data.replace(client.value, "");
	echoText.value += msg + "\n";
}
function wsClose(message)
{
    echoText.value += client.value + " Disconnected ... \n";
}
function wserror(message)
{
    echoText.value += "Error ... \n";
}


