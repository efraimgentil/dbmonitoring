package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.node.JsonNodeFactory;

import com.efraimgentil.dbmonitoring.models.JsonAction;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.services.MonitorService;

@WebServlet({ "/monitor" })
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		MonitorService monitorService = new MonitorService();
		
		String jsonString = request.getParameter("form");
		JsonAction action =  mapper.reader(JsonAction.class).readValue( jsonString );
		action.setJsonDataString( jsonString );
		MonitorResponse monitorResponse = monitorService.execute( action );
		mapper.writeValue(outputStream, monitorResponse);
	}
	
}