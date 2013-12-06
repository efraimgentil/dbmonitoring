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
		
		Map<String, Object> data = new HashMap<>();
		
		String action = request.getParameter("action") != null ? request.getParameter("action") : null;
		
		OutputStream outputStream = response.getOutputStream();
		
		ObjectMapper mapper = new ObjectMapper();
     	if ( INITIATE.equalsIgnoreCase(action) ) {
     		ObjectReader objectReader =  mapper.reader(MonitorInfo.class);
         	MonitorInfo monitorInfo = objectReader.readValue( request.getParameter("form") );
			try {
				ConnectionPool.getInstance().openConnection( monitorInfo );
				data.put("token", monitorInfo.getHost() );
				mapper.writeValue( outputStream ,  new MonitorResponse( true , "Connection was open with success" , data ) );
			} catch (ClassNotFoundException e) {
				mapper.writeValue( outputStream ,  new MonitorResponse( false , "Was not possible to find the connection driver" , null ) );
			} catch (SQLException e) {
				mapper.writeValue( outputStream ,  new MonitorResponse( false , "Was not possible to open the connection, verify the connection information. Error: " + e.getMessage() , null ) );
			}
			return;
		}

     	if (UPDATE.equalsIgnoreCase(action)) {
			Object monitorTitleO = request.getParameter("monitorTitle");
			Object tokenO = request.getParameter("token");
			Object queryO = request.getParameter("query");
			String token = tokenO != null ? (String) tokenO : null;
			String monitorTitle = monitorTitleO != null ? (String) monitorTitleO : null;
			String query = queryO != null ? (String) queryO : null;
			if ( query != null && ( token != null && !token.isEmpty() ) )
				try {
					Connection conn = ConnectionPool.getInstance().getConnection( token );
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd/MM/yyyy as HH:mm:ss");
					MonitorResponse monitorResponse = new MonitorResponse( true , null , new HashMap<String,Object>() );
					monitorResponse.getData().put("updateDate", sdf.format(new Date()) );
					monitorResponse.createRows(rs);
					mapper.writeValue( outputStream , monitorResponse );
				} catch (SQLException e) {
					mapper.writeValue( outputStream ,  new MonitorResponse( true , "Was not possible to execute the query. Error: " + e.getMessage() , null ) ); 
					e.printStackTrace();
				}
			else
				mapper.writeValue( outputStream ,  new MonitorResponse( true , "There is no query to be executed" , null ) );
			
			return;
		}
		mapper.writeValue( outputStream ,  new MonitorResponse( true , "This is not a valid action" , null ) );
	}

}
