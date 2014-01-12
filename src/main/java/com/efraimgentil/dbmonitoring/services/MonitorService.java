package com.efraimgentil.dbmonitoring.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.models.ConnectionInfo;
import com.efraimgentil.dbmonitoring.models.JsonAction;
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

	public static final String INITIATE = "INITIATE";
	public static final String UPDATE = "UPDATE";
	public static final String OPEN_CONNECTION = "OPEN_CONNECTION";

	private MonitorInfoPoolService monitorInfoPoolService;
	private ConnectionPool connectionPool;
	private QueryService queryService;
	private ResponseService responseService;
	
	public MonitorService() {
		queryService =  new QueryService();
		responseService =  new ResponseService();
	}

	public MonitorResponse execute(JsonAction jsonAction) {
		String action = jsonAction.getAction();
		switch (action.toUpperCase()) {
		case OPEN_CONNECTION:
			return openConnection( jsonAction );
		case INITIATE:
			return initiateMonitor( jsonAction );
		case UPDATE:
			return updateMonitor( jsonAction.getToken() );
		default:
			return new MonitorResponse(false, "This is not a valid action");
		}
	}

	public MonitorResponse openConnection(JsonAction jsonAction) {
		try {
			ConnectionInfo connectionInfo = createConnectionInfo( jsonAction.getJsonDataString() );
			connectionInfo = getConnectionPool().openConnection( connectionInfo );
			
			MonitorInfo monitorInfo = new MonitorInfo( connectionInfo );
			getMonitorInfoPoolService().addMonitor(monitorInfo);

			Map<String, Object> data = new HashMap<>();
			data.put("token", monitorInfo.getToken() );
			return responseService.createSuccessResponse( "Connection was open with success" , data);
		} catch (ClassNotFoundException e) {
			return responseService.createFailureResponse( "Was not possible to find the connection driver" );
		} catch (SQLException e) {
			return responseService.createFailureResponse("Was not possible to open the connection, verify the connection information. Error: "
					+ e.getMessage());
		} catch (Exception e) {
			return dealWithException(e);
		}
	}
	
	protected ConnectionInfo createConnectionInfo(String jsonString) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		ConnectionInfo connectionInfo = mapper.reader( ConnectionInfo.class ).readValue(jsonString);
		return connectionInfo;
	}

	public MonitorResponse initiateMonitor(JsonAction jsonAction) {
		try {
			String monitorToken = jsonAction.getToken();
			MonitorInfo monitorInfo = getMonitorInfoPoolService().updateMonitorInfoAndGet( monitorToken , jsonAction.getJsonDataMap() );
			ResultSet resultSet = getQueryService().executeQuery(monitorInfo);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", monitorToken );
			MonitorResponse monitorResponse = responseService.createSuccessResponse( "Monitor successfully initiated" , data, resultSet );
			
			if (monitorInfo.getSession() != null) {
				initiateMonitorThread( monitorInfo );
			}

			return monitorResponse;
		} catch (SQLException e) {
			return responseService.createFailureResponse( e , "Was not possible to initiate the monitor. Error: "
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

	protected MonitorResponse dealWithException(Exception e) {
		String message = null;
		if (e instanceof NoMonitorTokenException)
			message = "The token for monitor was not found Error:" + e.getMessage();
		else if (e instanceof WrongTokenFormatException)
			message = "The token has the wrong format. Error: " + e.getMessage();
		else if (e instanceof ConnectionNotFound)
			message = "The connection for this token was not found. Error: "+ e.getMessage();
		else
			message = "Unexpected error. Error: " + e.getMessage();
		
		return responseService.createFailureResponse( e , message);
	}
	
	protected void initiateMonitorThread(MonitorInfo monitorInfo) {
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
