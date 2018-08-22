package com.hgoyal.chatserver;

import javax.websocket.Session;

public class BotThread implements Runnable
{
	private Session session; //client chat session
	
	public BotThread(Session s)
	{
		this.session = s;
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				if (session.isOpen())
				{
					{
						session.getBasicRemote().sendText(Math.random() + " ");
						Thread.sleep(3000);
					}
				}
				else
				{
					return;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
