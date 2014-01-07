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
import com.efraimgentil.dbmonitoring.threads.MonitorThread;
import com.efraimgentil.dbmonitoring.utils.StringUtils;
import com.efraimgentil.dbmonitoring.websocket.WSPool;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 8, 2013
 */
public class MonitorService {

	private static final String INITIATE = "INITIATE";
	private static final String UPDATE = "UPDATE";
	private static final String OPEN_CONNECTION = "OPEN_CONNECTION";

	private StringUtils stringUtils;
	private MonitorInfoPoolService monitorInfoPoolService;
	private ConnectionPool connectionPool;

	public MonitorResponse execute(MonitorInfo monitorInfo) {
		String action = monitorInfo.getAction() != null ? monitorInfo
				.getAction() : "";
		switch (action.toUpperCase()) {
		case OPEN_CONNECTION:
			return openConnection(monitorInfo);
		case INITIATE:
			return initiateMonitor(monitorInfo);
		case UPDATE:
			return updateMonitor( monitorInfo.getToken());
		default:
			return new MonitorResponse(false, "This is not a valid action",
					null);
		}
	}

	public MonitorResponse openConnection(MonitorInfo monitorInfo) {
		try {
			String token = getConnectionPool().openConnection(monitorInfo);
			Map<String, Object> data = new HashMap<>();
			token += "-" + getStringUtils().md5(new Date().toString());
			data.put("token", token);
			monitorInfo.setToken(token);
			getMonitorInfoPoolService().addMonitor(monitorInfo);
			return new MonitorResponse(true,
					"Connection was open with success", data);
		} catch (ClassNotFoundException e) {
			return new MonitorResponse(false,
					"Was not possible to find the connection driver", null);
		} catch (SQLException e) {
			return new MonitorResponse(
					false,
					"Was not possible to open the connection, verify the connection information. Error: "
							+ e.getMessage(), null);
		}
	}

	public MonitorResponse initiateMonitor(MonitorInfo monitorInfo) {
		try {
			Connection conn = getConnectionPool().getConnection(
					monitorInfo.getConnectionToken());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(monitorInfo.getQuery());
			monitorInfo = getMonitorInfoPoolService().updateMappedMonitorInfo(monitorInfo);
			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", monitorInfo.getToken());
			MonitorResponse monitorResponse = new MonitorResponse(true,
					"Monitor successfully initiated", data);
			monitorResponse.createRows(rs);
			
			if( monitorInfo.getSession() != null ){
				initiateMonitorThread(monitorInfo);
			}
			
			return monitorResponse;
		} catch (SQLException e) {
			return new MonitorResponse(false,
					"Was not possible to initiate the monitor. Error: "
							+ e.getMessage(), null);
		} catch (Exception e) {
			return dealWithException(e);
		}
	}

	public MonitorResponse updateMonitor(String token) {
		try {
			MonitorInfo monitorInfo = getMonitorInfoPoolService().getMonitor(
					token);
			Connection conn = ConnectionPool.getInstance().getConnection(
					monitorInfo.getConnectionToken());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(monitorInfo.getQuery());
			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd/MM/yyyy as HH:mm:ss");
			MonitorResponse monitorResponse = new MonitorResponse(true, null,
					new HashMap<String, Object>());
			monitorResponse.getData().put("updateDate", sdf.format(new Date()));
			monitorResponse.createRows(rs);
			return monitorResponse;
		} catch (SQLException e) {
			return new MonitorResponse(false,
					"Was not possible to execute the query. Error: "
							+ e.getMessage(), null);
		} catch (Exception e) {
			return dealWithException(e);
		}
	}

	public MonitorResponse dealWithException(Exception e) {
		if (e instanceof NoMonitorTokenException)
			return new MonitorResponse(false,
					"The token for monitor was not found Error:"
							+ e.getMessage(), null);
		else if (e instanceof WrongTokenFormatException)
			return new MonitorResponse(false,
					"The token has the wrong format. Error: " + e.getMessage(),
					null);
		else if (e instanceof ConnectionNotFound)
			return new MonitorResponse(false,
					"The connection for this token was not found. Error: "
							+ e.getMessage(), null);
		else
			return new MonitorResponse(false, "Unexpected error. Error: "
					+ e.getMessage(), null);
	}
	
	public void initiateMonitorThread(MonitorInfo monitorInfo){
		System.out.println(" Initiating Monitor ");
		WSPool.getInstance().addClient(monitorInfo);
		new Thread( new MonitorThread( monitorInfo.getToken() ) ).start(); 
	}

	public StringUtils getStringUtils() {
		return stringUtils == null ? stringUtils = new StringUtils()
				: stringUtils;
	}

	public void setStringUtils(StringUtils stringUtils) {
		this.stringUtils = stringUtils;
	}

	public ConnectionPool getConnectionPool() {
		if (connectionPool == null) {
			connectionPool = ConnectionPool.getInstance();
		}
		return connectionPool;
	}

	public void setConnectionPool(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public MonitorInfoPoolService getMonitorInfoPoolService() {
		return monitorInfoPoolService == null ? monitorInfoPoolService = MonitorInfoPoolService
				.getInstance() : monitorInfoPoolService;
	}

	public void setMonitorInfoPoolService(
			MonitorInfoPoolService monitorInfoPoolService) {
		this.monitorInfoPoolService = monitorInfoPoolService;
	}

}
