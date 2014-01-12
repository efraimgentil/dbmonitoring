package com.efraimgentil.dbmonitoring.services;

import java.util.HashMap;
import java.util.Map;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.threads.MonitorThread;

public class MonitorThreadService {
	
	private Map<String , MonitorThread> threadMap;
	
	public MonitorThreadService() {
		threadMap = new HashMap<String, MonitorThread>();
	}
	
	public void registerMonitor(MonitorInfo monitorInfo){
		String token = monitorInfo.getToken();
		if( shouldCreateANewThread( token ) );
			createNewThread(token);
	}
	
	protected void createNewThread(String token){
		 MonitorThread mt = new MonitorThread( token );
		 threadMap.put( token, mt );
		 new Thread(mt).start();
	}
	
	protected void stopThread(String token){
		MonitorThread mt = threadMap.get(token);
		mt.setRunning(false);
		threadMap.put( token , null );
		threadMap.remove(token);
	} 
	
	public boolean shouldCreateANewThread( String token ){
		return !threadMap.containsKey(token);
	}
	
	
}