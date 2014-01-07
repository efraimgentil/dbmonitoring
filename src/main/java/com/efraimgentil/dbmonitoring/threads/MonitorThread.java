package com.efraimgentil.dbmonitoring.threads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.websocket.Session;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;
import com.efraimgentil.dbmonitoring.websocket.WSPool;

public class MonitorThread implements Runnable {

	private String monitorToken;

	public MonitorThread(String monitorToken) {
		this.monitorToken = monitorToken;
	}

	@Override
	public void run() {
		WSPool wsPool = WSPool.getInstance();

		ObjectMapper mapper = new ObjectMapper();
		MonitorInfo monitorInfo = MonitorInfoPoolService.getInstance()
				.getMonitor(monitorToken);
		
		
		long refreshTime = monitorInfo.getRefreshTime() != null ? monitorInfo.getRefreshTime() : 5 ;
		try {
			System.out.println("Iniciando Monitor Thread");
			while (wsPool.hasSessions(monitorToken)) {
				Thread.sleep(  refreshTime * 1000 );
				List<Session> sessions = wsPool.getSessions(monitorToken);

				MonitorResponse monitorResponse = new MonitorService()
						.updateMonitor(monitorToken);

				for (Session session : sessions) {
					try {
						String monitorResponseAsString = mapper.writeValueAsString(monitorResponse);
//						session.getBasicRemote().sendBinary( ByteBuffer.wrap(  monitorResponseAsString.getBytes() ) );
						session.getBasicRemote().sendText( monitorResponseAsString );
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				System.out.println("Will wait " + monitorInfo.getRefreshTime()
						+ " seconds util next update");
				monitorInfo = MonitorInfoPoolService.getInstance().getMonitor(
						monitorToken);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
