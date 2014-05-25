package com.efraimgentil.dbmonitoring.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.models.builder.ResponseBuilder;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.utils.StringUtils;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 8, 2013
 */
public class MonitorService {
	
	private MonitorInfoPoolService monitorInfoPoolService;
	private ConnectionPool connectionPool;
	
	public MonitorService(MonitorInfoPoolService monitorInfoPoolService,
			ConnectionPool connectionPool) {
		super();
		this.monitorInfoPoolService = monitorInfoPoolService;
		this.connectionPool = connectionPool;
	}

	public MonitorResponse openConnection(MonitorInfo monitorInfo){
		MonitorResponse response;
		try {
			String token = getConnectionPool().openConnection(monitorInfo);
			Map<String, Object> data = new HashMap<>();
			token += "-" + new StringUtils().md5( new Date().toString() );
			data.put("token", token );
			monitorInfo.setToken(token);
			getMonitorInfoPoolService().addMonitor(monitorInfo);
			response = new ResponseBuilder().success()
					.withMessage( "Connection was open with success" )
					.withData(data).build();
		} catch (ClassNotFoundException e) {
			response = createErrorReponse( "Was not possible to find the connection driver" );
		} catch (SQLException e) {
			String message = "Was not possible to open the connection, verify the connection information. Error: "
					+ e.getMessage();
			response = createErrorReponse( message);
		}
		return response;
	}
	
	public MonitorResponse initiateMonitor(MonitorInfo monitorInfo){
		MonitorResponse response;
		try {
			Connection conn = getConnectionPool().getConnection( monitorInfo.getConnectionToken() );
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( monitorInfo.getQuery() );
			getMonitorInfoPoolService().updateMappedMonitorInfo(monitorInfo);
			
			response = new ResponseBuilder().success()
					.withMessage( "Monitor successfully initiated" )
					.putData("token", monitorInfo.getToken() )
					.readResultSetToDataRows( rs )
					.build();
		} catch (SQLException e) {
			String message =  "Was not possible to initiate the monitor. Error: " + e.getMessage();
			response =  createErrorReponse(message);
		} catch (Exception e) {
			response = dealWithException(e);
		}
		return response;
	}
	
	public MonitorResponse processQuery( String token ){
		try {
			MonitorInfo monitorInfo = getMonitorInfoPoolService().getMonitor( token );
			Connection conn = getConnectionPool().getConnection( monitorInfo.getConnectionToken() );
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( monitorInfo.getQuery() );
			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd/MM/yyyy as HH:mm:ss");
			
			return new ResponseBuilder().success()
				.putData("updateDate" , sdf.format(new Date()) )
				.readResultSetToDataRows( rs ).build();
		} catch (SQLException e) {
			String message = "Was not possible to execute the query. Error: " + e.getMessage(); 
			return createErrorReponse( message );
		} catch (Exception e) {
			return dealWithException(e);
		}
	}
	
	public MonitorResponse dealWithException(Exception e){
		return createErrorReponse( e.getMessage() );
	}
	
	public MonitorResponse createErrorReponse( String errorMessage ){
		return new ResponseBuilder().error().withMessage( errorMessage ).build();
	}
	
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	public MonitorInfoPoolService getMonitorInfoPoolService() {
		return monitorInfoPoolService;
	}
	
}
