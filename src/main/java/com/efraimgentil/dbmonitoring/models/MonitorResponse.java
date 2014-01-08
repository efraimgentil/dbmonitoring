package com.efraimgentil.dbmonitoring.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public class MonitorResponse {
	
	@JsonProperty("success")
	private Boolean success;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("data")
	private Map<String, Object> data;
	
	public MonitorResponse() {	}
	
	public MonitorResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
		this.data = null;
	}
	
	public MonitorResponse(Boolean success, String message, Map<String, Object> data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		if(data == null){
			data = new HashMap<>();
		}
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void createRows(ResultSet resultSet) throws SQLException{
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
		getData().put("rows", rows);
	}
	
}