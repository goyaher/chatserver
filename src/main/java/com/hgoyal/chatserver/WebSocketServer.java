package com.hgoyal.chatserver;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint/{user-id}")
public class WebSocketServer 
{
	@OnOpen
	public void onOpen(Session session, EndpointConfig config, @PathParam("user-id") String userId) throws IOException
	{
		MessageUtil.addSession(userId, session);
		System.out.println("connection opened by user " + userId);
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) throws IOException
	{	
		//from:to:msg
		Message message = MessageUtil.getMessage(msg);
		System.out.println(":" + message.getMessage());
		MessageUtil.send(message);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException
	{
		String userId = (String)(session.getUserProperties().get("userID"));
		MessageUtil.removeSession(userId);
		System.out.println("connection closed by user " + userId);
	}
	
	@OnError
	public void onError(Session session, Throwable e)
	{
	    e.printStackTrace();
	}

}
