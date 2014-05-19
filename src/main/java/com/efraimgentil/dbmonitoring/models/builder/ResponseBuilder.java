package com.efraimgentil.dbmonitoring.models.builder;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.exceptions.Reporter;

import com.efraimgentil.dbmonitoring.models.MonitorResponse;

public class ResponseBuilder {
	
	private MonitorResponse response;
	
	public ResponseBuilder() {
		response = new MonitorResponse();
	}
	
	public ResponseBuilder error(){
		response.setSuccess(false);
		return this;
	}
	
	public ResponseBuilder success(){
		response.setSuccess( true );
		return this;
	}
	
	public ResponseBuilder withMessage(String message){
		response.setMessage(message);
		return this;
	}
	
	public ResponseBuilder withData(Map<String, Object> data){
		response.setData(data);
		return this;
	}
	
	public ResponseBuilder putData(String key , Object value){
		Map<String, Object> data = response.getData();
		if(data == null){
			data = new HashMap<String, Object>();
			response.setData(data);
		}
		data.put(key, value);
		return this;
	}
	
	public ResponseBuilder readResultSetToDataRows(ResultSet resultSet) throws SQLException{
		List< Map<String, Object> > rows = new ArrayList<>(); 
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map<String, Object> row;
		String columnLabel;
		Object columnValue;
		while( resultSet.next() ){
			row = new HashMap<String, Object>();
			for(int index = 1; index <= columnCount ; index++){
				columnLabel = rsmd.getColumnLabel(index);
				columnValue = resultSet.getObject(index);
				if (columnValue == null) {
					row.put(columnLabel , null);
	            } else if (columnValue instanceof Integer) {
	            	row.put(columnLabel, (Integer) columnValue);
	            } else if (columnValue instanceof String) {
	                row.put(columnLabel, (String) columnValue);                
	            } else if (columnValue instanceof Boolean) {
	                row.put(columnLabel, (Boolean) columnValue);           
	            } else if (columnValue instanceof Date) {
	                row.put(columnLabel, ((Date) columnValue) );                
	            } else if (columnValue instanceof Long) {
	                row.put(columnLabel, (Long) columnValue);                
	            } else if (columnValue instanceof Double) {
	                row.put(columnLabel, (Double) columnValue);                
	            } else if (columnValue instanceof Float) {
	                row.put(columnLabel, (Float) columnValue);                
	            } else if (columnValue instanceof BigDecimal) {
	                row.put(columnLabel, (BigDecimal) columnValue);
	            } else if (columnValue instanceof Byte) {
	                row.put(columnLabel, (Byte) columnValue);
	            } else if (columnValue instanceof byte[]) {
	                row.put(columnLabel, (byte[]) columnValue);                
	            } else {
	                throw new IllegalArgumentException("Unmappable object type: " + columnValue.getClass());
	            }	
			}
			rows.add(row);
		}
		putData("rows", rows);
		return this;
	}
	
	public MonitorResponse build(){
		return response;
	}
	
}