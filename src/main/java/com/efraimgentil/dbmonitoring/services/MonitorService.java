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
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.utils.StringUtils;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 8, 2013
 */
public class MonitorService {
	
	private StringUtils stringUtils;
	
	public MonitorResponse openConnection(MonitorInfo monitorInfo){
		try {
			stringUtils = new StringUtils();
			ConnectionPool.getInstance().openConnection(monitorInfo);
			Map<String, Object> data = new HashMap<>();
			data.put("token", stringUtils.md5( monitorInfo.getHost() ) );
			return new MonitorResponse(true, "Connection was open with success", data);
		} catch (ClassNotFoundException e) {
			return new MonitorResponse(false, "Was not possible to find the connection driver", null );
		} catch (SQLException e) {
			return new MonitorResponse(false,
							"Was not possible to open the connection, verify the connection information. Error: "
							+ e.getMessage(), null );
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
			return new MonitorResponse( false , "Unexpected error. Error: " + e.getMessage() , null );
		}
	}
	
}
