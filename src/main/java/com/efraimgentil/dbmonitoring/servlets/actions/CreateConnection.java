package com.efraimgentil.dbmonitoring.servlets.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;

public class CreateConnection implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectReader reader = mapper.reader(MonitorInfo.class);
			
			MonitorInfo monitorInfo = reader.readValue( request.getParameter("form") );
			MonitorService monitorService = new MonitorService( MonitorInfoPoolService.getInstance() , ConnectionPool.getInstance() );
			MonitorResponse monitorResponse = monitorService.openConnection(monitorInfo);
			
			mapper.writeValue( response.getOutputStream() , monitorResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
