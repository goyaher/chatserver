package com.hgoyal.chatserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

/**
 * 
 * @author Hershit
 *
 */
public class MessageUtil 
{
	private static Map<String, Session> activeSessions = new HashMap<String, Session>();
	
	public static void addSession(String userId, Session session) throws IOException
	{
		activeSessions.put(userId, session);
		session.getUserProperties().put("userID", userId);
		
		//broadcast that I am online to others
		sendIAmOnlineMessage(userId, true);
		
		//send to me that who all are online
		sendWhoAllAreOnlineMessage(userId);
	}
	
	public static void removeSession(String userId) throws IOException
	{
		//broadcast that I am online to others
		sendIAmOnlineMessage(userId, false);
		
		activeSessions.remove(userId);
	}
	
	public static Message getMessage(String messageString)
	{
		String[] splits = messageString.split(":");
		Message message = new Message(splits[0], splits[1], splits[2]);
		System.out.println(message);
		return message;
	}
	
	public static void send(Message message) throws IOException
	{
		Session session = activeSessions.get(message.getTo());
		session.getBasicRemote().sendText(message.getSerializedMessage());
	}
	
	/**
	 * Broadcasts to all online/offline users that given userId is online.
	 * 
	 * @param userId
	 * @throws IOException
	 */
	public static void sendIAmOnlineMessage(String userId, boolean online) throws IOException
	{
		Message onLineMessage = new Message("SERVER", "", userId + (online ? " is ONLINE" : " is OFFLINE"));
		
		for (String key: activeSessions.keySet())
		{
			if (!userId.equals(key))
			{
				activeSessions.get(key).getBasicRemote().sendText(onLineMessage.getSerializedMessage());
			}
		}
	}
	
	public static void sendWhoAllAreOnlineMessage(String userId) throws IOException
	{
		boolean isAtleastOneOnline = false;
		for (String key: activeSessions.keySet())
		{
			if (!userId.equals(key))
			{
				isAtleastOneOnline = true;
				Message onLineMessage = new Message("SERVER", "", key + " is ONLINE");
				activeSessions.get(userId).getBasicRemote().sendText(onLineMessage.getSerializedMessage());
			}
		}
		if (!isAtleastOneOnline)
		{
			Message onLineMessage = new Message("SERVER", "", "No-user is ONLINE yet");
			activeSessions.get(userId).getBasicRemote().sendText(onLineMessage.getSerializedMessage());
		}
	}
}
