package com.efraimgentil.dbmonitoring.servlets;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/webs/monitor")
public class WSMonitorServlet {

	public WSMonitorServlet() {
	}

	@OnOpen
	public void open(Session session) {
		System.out.println("OPEN ");
	}

	@OnClose
	public void close() {
		System.out.println("CLOSE ");
	}

	@OnMessage
	public String handleMessage(String message) {
		return "Got your message (" + message + "). Thanks !";
	}

}
