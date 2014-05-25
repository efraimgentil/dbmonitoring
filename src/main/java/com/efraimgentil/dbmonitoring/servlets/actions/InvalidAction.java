package com.efraimgentil.dbmonitoring.servlets.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.models.builder.ResponseBuilder;

public class InvalidAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();

			MonitorResponse monitorResponse = new ResponseBuilder().error().withMessage("This is not a valid action" ).build();
			mapper.writeValue( response.getOutputStream() , monitorResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
