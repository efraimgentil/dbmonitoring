package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.services.MonitorService;

@WebServlet({ "/monitor" })
public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String INITIATE = "INITIATE";
	private static final String UPDATE = "UPDATE";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		MonitorService monitorService = new MonitorService();

		String action = request.getParameter("action") != null ? request
				.getParameter("action").toUpperCase() : null;

		OutputStream outputStream = response.getOutputStream();

		ObjectMapper mapper = new ObjectMapper();
		
		switch (action) {
			case INITIATE:
				ObjectReader objectReader = mapper.reader(MonitorInfo.class);
				MonitorInfo monitorInfo = objectReader.readValue(request.getParameter("form"));
				MonitorResponse monitorResponse = monitorService.openConnection(monitorInfo);
				mapper.writeValue(outputStream, monitorResponse);
				break;
			case UPDATE:
				Object monitorTitleO = request.getParameter("monitorTitle");
				Object tokenO = request.getParameter("token");
				Object queryO = request.getParameter("query");
				String token = tokenO != null ? (String) tokenO : null;
				String monitorTitle = monitorTitleO != null ? (String) monitorTitleO
						: null;
				String query = queryO != null ? (String) queryO : null;
				if (query != null && (token != null && !token.isEmpty())) {
					mapper.writeValue(outputStream,
							monitorService.processQuery(token, query));
				} else {
					mapper.writeValue(outputStream, new MonitorResponse(false,
							"There is no query to be executed", null));
				}
				break;
			default:
				mapper.writeValue(outputStream, new MonitorResponse(false, "This is not a valid action" , null));
				break;
		}

	}

}