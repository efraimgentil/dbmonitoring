package com.efraimgentil.dbmonitoring.servlets.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;

public class UpdateMonitor implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			MonitorService monitorService = new MonitorService( MonitorInfoPoolService.getInstance() , ConnectionPool.getInstance() );
			
			Object tokenO = request.getParameter("token");
			String token = tokenO != null ? (String) tokenO : null;
		
			mapper.writeValue( response.getOutputStream() , monitorService.processQuery(token ));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
