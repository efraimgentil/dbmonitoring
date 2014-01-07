package com.efraimgentil.dbmonitoring.websocket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;

public class WSPool {
	
	private Map< String , List<Session> > clients;
	private static WSPool instance;
	
	private WSPool(){
		clients = new ConcurrentHashMap<>();
	}
	
	public static WSPool getInstance(){
		if(instance == null){
			instance = new WSPool();
		}
		return instance;
	}
	
	public void addClient( MonitorInfo monitorInfo ){
	   if( clients.containsKey( monitorInfo.getToken() ) ){
		   List<Session> sessions = clients.get( monitorInfo.getToken() );
		   sessions.add( monitorInfo.getSession() );
	   }else{
		   clients.put( monitorInfo.getToken() , Arrays.asList( monitorInfo.getSession() ) );
	   }
	}
	
	public void removeClient(MonitorInfo monitorInfo){
		List<Session> sessions = clients.get( monitorInfo.getToken() );
		sessions.remove( monitorInfo.getSession() );
		if(sessions.size() == 0)
			clients.remove( monitorInfo.getToken() );
	}
	
	public List<Session> getSessions(String monitorToken){
		return clients.get(monitorToken);
	}
	
	public boolean hasSessions( String monitorToken ){
		return clients.containsKey( monitorToken ) && !clients.get(monitorToken).isEmpty();
	}
	
}