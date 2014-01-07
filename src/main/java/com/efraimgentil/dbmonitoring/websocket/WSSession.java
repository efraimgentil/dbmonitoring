package com.efraimgentil.dbmonitoring.websocket;

import javax.websocket.Session;

public class WSSession {

	private String monitorToken;
	
	private Session session;
	
	public WSSession() {}

	public WSSession(String monitorToken, Session session) {
		super();
		this.monitorToken = monitorToken;
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getMonitorToken() {
		return monitorToken;
	}



	public void setMonitorToken(String monitorToken) {
		this.monitorToken = monitorToken;
	}
	
}