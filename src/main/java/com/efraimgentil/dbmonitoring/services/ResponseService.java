package com.efraimgentil.dbmonitoring.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.efraimgentil.dbmonitoring.models.MonitorResponse;

public class ResponseService {
	
	public MonitorResponse createFailureResponse(String message){
		return createFailureResponse( null , message );
	}
	
	public MonitorResponse createFailureResponse( Exception exception , String message ){
		return createFailureResponse( exception , null , message );
	}
	
	public MonitorResponse createFailureResponse( Map<String , Object> data ){
		return createFailureResponse(null ,  data , null  );
	}
	
	public MonitorResponse createFailureResponse(Exception exception , Map<String , Object> data , String message ){
		return new MonitorResponse(false , message, data , exception);
	}
	
	public MonitorResponse createSuccessResponse( String message ) throws SQLException{
		return createSuccessResponse( message, null );
	}
	
	public MonitorResponse createSuccessResponse( Map<String, Object> data ) throws SQLException{
		return createSuccessResponse( null , data);
	}
	
	public MonitorResponse createSuccessResponse(String message , Map<String, Object> data ) throws SQLException{
		return createSuccessResponse( message, data, null);
	}
	
	public MonitorResponse createSuccessResponse( Map<String, Object> data , ResultSet resultSet ) throws SQLException{
		return createSuccessResponse( null , data, resultSet );
	}
	
	public MonitorResponse createSuccessResponse(String message , Map<String, Object> data , ResultSet resultSet ) throws SQLException{
		MonitorResponse monitorResponse = new MonitorResponse(true , message , data);
		if( shouldCreateRows(resultSet) )
			monitorResponse.createRows(resultSet);
		return monitorResponse;
	}
	
	protected boolean shouldCreateRows(ResultSet resultSet){
		return resultSet != null; 
	}
	
}
