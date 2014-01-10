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

	private MonitorInfoPoolService monitorInfoPoolService;
	private ConnectionPool connectionPool;
	private QueryService queryService;
	private ResponseService responseService;
	
	public MonitorService() {
		queryService =  new QueryService();
		responseService =  new ResponseService();
	}

	public MonitorResponse execute(MonitorInfo monitorInfo) {
		String action = monitorInfo.getAction() != null ? monitorInfo
				.getAction() : "";
		switch (action.toUpperCase()) {
		case OPEN_CONNECTION:
			return openConnection(monitorInfo);
		case INITIATE:
			return initiateMonitor(monitorInfo);
		case UPDATE:
			return updateMonitor(monitorInfo.getToken());
		default:
			return new MonitorResponse(false, "This is not a valid action");
		}
	}

	public MonitorResponse openConnection(MonitorInfo monitorInfo) {
		try {
			monitorInfo = getConnectionPool().openConnection(monitorInfo);

			monitorInfo.generateToken();
			getMonitorInfoPoolService().addMonitor(monitorInfo);

			Map<String, Object> data = new HashMap<>();
			data.put("token", monitorInfo.getToken() );
			return responseService.createSuccessResponse( "Connection was open with success" , data);
		} catch (ClassNotFoundException e) {
			return responseService.createFailureResponse( "Was not possible to find the connection driver" );
		} catch (SQLException e) {
			return responseService.createFailureResponse("Was not possible to open the connection, verify the connection information. Error: "
					+ e.getMessage());
		}
	}

	public MonitorResponse initiateMonitor(MonitorInfo monitorInfo) {
		try {
			ResultSet resultSet = getQueryService().executeQuery(monitorInfo);
			monitorInfo = getMonitorInfoPoolService().updateMappedMonitorInfo(
					monitorInfo);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", monitorInfo.getToken());
			MonitorResponse monitorResponse = responseService.createSuccessResponse( "Monitor successfully initiated" , data, resultSet );
			
			if (monitorInfo.getSession() != null) {
				initiateMonitorThread(monitorInfo);
			}

			return monitorResponse;
		} catch (SQLException e) {
			return responseService.createFailureResponse( "Was not possible to initiate the monitor. Error: "
					+ e.getMessage() );
		} catch (Exception e) {
			return dealWithException(e);
		}
	}

	public MonitorResponse updateMonitor(String token) {
		try {
			MonitorInfo monitorInfo = getMonitorInfoPoolService().getMonitor(
					token);
			ResultSet resultSet = getQueryService().executeQuery(monitorInfo);
			
			Map<String, Object> data = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			data.put("lastUpdateDate", sdf.format(new Date()));
			
			return responseService.createSuccessResponse( data , resultSet);
		} catch (SQLException e) {
			return responseService.createFailureResponse( "Was not possible to execute the query. Error: "
					+ e.getMessage() );
		} catch (Exception e) {
			return dealWithException(e);
		}
	}

	public MonitorResponse dealWithException(Exception e) {
		String message = null;
		if (e instanceof NoMonitorTokenException)
			message = "The token for monitor was not found Error:" + e.getMessage();
		else if (e instanceof WrongTokenFormatException)
			message = "The token has the wrong format. Error: " + e.getMessage();
		else if (e instanceof ConnectionNotFound)
			message = "The connection for this token was not found. Error: "+ e.getMessage();
		else
			message = "Unexpected error. Error: " + e.getMessage();
		
		return responseService.createFailureResponse(message);
	}

	public void initiateMonitorThread(MonitorInfo monitorInfo) {
		System.out.println(" Initiating Monitor ");
		WSPool.getInstance().addClient(monitorInfo);
		new Thread(new MonitorThread(monitorInfo.getToken())).start();
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

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

	public ResponseService getResponseService() {
		return responseService;
	}

	public void setResponseService(ResponseService responseService) {
		this.responseService = responseService;
	}

}
