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
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.utils.StringUtils;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 8, 2013
 */
public class MonitorService {
	
	private static HashMap<String, MonitorInfo> monitors = new HashMap<>();
	
	private StringUtils stringUtils;
	
	public MonitorResponse openConnection(MonitorInfo monitorInfo){
		try {
			String token = ConnectionPool.getInstance().openConnection(monitorInfo);
			Map<String, Object> data = new HashMap<>();
			token = "-" + stringUtils.md5( new Date().toString() );
			data.put("token", token );
			return new MonitorResponse(true, "Connection was open with success", data);
		} catch (ClassNotFoundException e) {
			return new MonitorResponse(false, "Was not possible to find the connection driver", null );
		} catch (SQLException e) {
			return new MonitorResponse(false,
							"Was not possible to open the connection, verify the connection information. Error: "
							+ e.getMessage(), null );
		}
	}
	
	public MonitorResponse initiateMonitor(MonitorInfo monitorInfo){
		try {
			updateMappedMonitorInfo(monitorInfo);
			Connection conn = ConnectionPool.getInstance().getConnection( monitorInfo.getConnectionToken() );
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery( monitorInfo.getQuery() );
			MonitorResponse monitorResponse = new MonitorResponse( true , "Monitor successfully initiated" , new HashMap<String,Object>() );
			monitorResponse.createRows(rs);
			return monitorResponse;
		} catch (SQLException e) {
			return new MonitorResponse( false , "Was not possible to initiate the monitor. Error: " + e.getMessage() , null );
		} catch (Exception e) {
			return dealWithException(e);
		}
	}
	
	public MonitorResponse processQuery( String token , String query ){
		try {
			Connection conn = ConnectionPool.getInstance().getConnection( token );
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd/MM/yyyy as HH:mm:ss");
			MonitorResponse monitorResponse = new MonitorResponse( true , null , new HashMap<String,Object>() );
			monitorResponse.getData().put("updateDate", sdf.format(new Date()) );
			monitorResponse.createRows(rs);
			return monitorResponse;
		} catch (SQLException e) {
			return new MonitorResponse( false , "Was not possible to execute the query. Error: " + e.getMessage() , null ); 
		} catch (Exception e) {
			return dealWithException(e);
		}
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public MonitorResponse dealWithException(Exception e){
		if(e instanceof NoMonitorTokenException)
			return new MonitorResponse( false , "The token for monitor was not found Error:" + e.getMessage() , null );
		else if (e instanceof WrongTokenFormatException )
			return new MonitorResponse( false , "The token has the wrong format. Error: " + e.getMessage() , null );
		else if ( e instanceof ConnectionNotFound )
			return new MonitorResponse( false , "The connection for this token was not found. Error: " + e.getMessage() , null );
		else
			return new MonitorResponse( false , "Unexpected error. Error: " + e.getMessage() , null );
	}
	
	/**
	 * 
	 * @param monitorInfo
	 * @return
	 * @throws WrongTokenFormatException 
	 * @throws NoMonitorTokenException 
	 */
	public MonitorInfo updateMappedMonitorInfo( MonitorInfo monitorInfo ) throws NoMonitorTokenException, WrongTokenFormatException{
		MonitorInfo mappedMonitor = monitors.get( monitorInfo.getToken() );
		mappedMonitor.setQuery( monitorInfo.getQuery() );
		mappedMonitor.setMonitorTitle( monitorInfo.getMonitorTitle() );
		mappedMonitor.setRefreshTime( monitorInfo.getRefreshTime() );
		String monitorToken = getStringUtils().md5( monitorInfo.getQuery() );
		monitorInfo.setMonitorToken(monitorToken);
		return mappedMonitor;
	}
	
	public StringUtils getStringUtils() {
		return stringUtils == null ? stringUtils = new StringUtils() : stringUtils;
	}

	public void setStringUtils(StringUtils stringUtils) {
		this.stringUtils = stringUtils;
	}
	
}
