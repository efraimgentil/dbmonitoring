package com.efraimgentil.dbmonitoring.threads;

import java.util.List;

import javax.websocket.Session;

import org.codehaus.jackson.map.ObjectMapper;

import com.efraimgentil.dbmonitoring.models.JsonAction;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;
import com.efraimgentil.dbmonitoring.websocket.WSPool;

public class MonitorThread implements Runnable {

	private static final long DEFAULT_REFRESH_TIME = 5;
	
	private String monitorToken;
	private boolean running = true;
	private WSPool wsPool;

	public MonitorThread(String monitorToken) {
		this.monitorToken = monitorToken;
	}

	@Override
	public void run() {
		
		wsPool = WSPool.getInstance();

		MonitorInfo monitorInfo = MonitorInfoPoolService.getInstance().getMonitor(monitorToken);
		
		Integer monitorRefreshTime =  monitorInfo.getRefreshTime();
		long refreshTime = monitorRefreshTime != null ? monitorRefreshTime.longValue() : DEFAULT_REFRESH_TIME ;
		try {
			System.out.println("Iniciando Monitor Thread");
			while ( wsPool.hasSessions(monitorToken) && isRunning() ) {
				waitRefreshTime( refreshTime );
				
				MonitorResponse monitorResponse = new MonitorService().updateMonitor( monitorToken );
				broadcastResponse( monitorResponse );
				
				System.out.println("Will wait " + monitorInfo.getRefreshTime()
						+ " seconds util next update");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void waitRefreshTime( long refreshTime ) throws InterruptedException{
		Thread.sleep(  refreshTime * 1000 );
	}
	
	public void broadcastResponse(MonitorResponse monitorResponse){
		List<Session> sessions = wsPool.getSessions(monitorToken);
		ObjectMapper mapper = new ObjectMapper();
		for (Session session : sessions) {
			try {
				String monitorResponseAsString = mapper.writeValueAsString(monitorResponse);
				session.getBasicRemote().sendText( monitorResponseAsString );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	} 

}
