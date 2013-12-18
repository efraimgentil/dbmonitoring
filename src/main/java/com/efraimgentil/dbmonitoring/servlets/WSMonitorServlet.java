package com.efraimgentil.dbmonitoring.servlets;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/monitor")
public class WSMonitorServlet {
	
	
	@OnOpen
	public void open(Session session){
		System.out.println("OPEN ");
	}
	
	
	@OnClose
	public void close(){
		System.out.println("CLOSE ");
	}
	
}
