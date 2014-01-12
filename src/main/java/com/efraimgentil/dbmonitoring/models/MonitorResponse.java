package com.efraimgentil.dbmonitoring.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public class MonitorResponse {

	@JsonIgnore
	private static final List<Class<? extends Serializable>> VALID_TYPES = Arrays
			.asList(Integer.class, String.class, Boolean.class, Date.class,
					Long.class, Double.class, Float.class, BigDecimal.class,
					Byte.class, byte[].class);

	@JsonProperty("success")
	private Boolean success;

	@JsonProperty("message")
	private String message;

	@JsonProperty("data")
	private Map<String, Object> data;
	
	@JsonIgnore
	private Exception exception;

	public MonitorResponse() {
	}

	public MonitorResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
		this.data = null;
	}

	public MonitorResponse(Boolean success, String message,
			Map<String, Object> data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}
	
	public MonitorResponse(Boolean success, String message,
			Map<String, Object> data , Exception exception) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
		this.exception = exception;
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
		if (data == null) {
			data = new HashMap<>();
		}
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void createRows(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> rows = new ArrayList<>();
		List<String> columnLabels = getColumnLabels(resultSet.getMetaData());
		while (resultSet.next()) {
			rows.add(createRow(resultSet, columnLabels));
		}
		getData().put("rows", rows);
	}

	protected List<String> getColumnLabels(ResultSetMetaData rsMetaData)
			throws SQLException {
		List<String> colummLabels = new ArrayList<>();
		for (int index = 1, columnCount = rsMetaData.getColumnCount(); index <= columnCount; index++) {
			colummLabels.add(rsMetaData.getColumnLabel(index));
		}
		return colummLabels;
	}

	public Map<String, Object> createRow(ResultSet resultSet,
			List<String> columnLabels) throws SQLException {
		Map<String, Object> row = new HashMap<String, Object>();
		Object columnValue;
		for (String columnLabel : columnLabels) {
			columnValue = resultSet.getObject(columnLabel);
			if (isValidValue(columnValue))
				row.put(columnLabel, columnValue );
			else
				throw new IllegalArgumentException("Unmappable object type: "
						+ columnValue.getClass() + " at column " + columnLabel);
		}
		return row;
	}

	protected Boolean isValidValue(Object columnValue) {
		if (columnValue == null || VALID_TYPES.contains(columnValue.getClass()))
			return true;
		else
			return false;
	}

	@Deprecated
	protected Object getColumnWrapedValue(Object columnValue) {
		if (columnValue == null) {
			return null;
		} else if (columnValue instanceof Integer) {
			return (Integer) columnValue;
		} else if (columnValue instanceof String) {
			return (String) columnValue;
		} else if (columnValue instanceof Boolean) {
			return (Boolean) columnValue;
		} else if (columnValue instanceof Date) {
			return ((Date) columnValue);
		} else if (columnValue instanceof Long) {
			return (Long) columnValue;
		} else if (columnValue instanceof Double) {
			return (Double) columnValue;
		} else if (columnValue instanceof Float) {
			return (Float) columnValue;
		} else if (columnValue instanceof BigDecimal) {
			return (BigDecimal) columnValue;
		} else if (columnValue instanceof Byte) {
			return (Byte) columnValue;
		} else if (columnValue instanceof byte[]) {
			return (byte[]) columnValue;
		} else {
			throw new IllegalArgumentException("Unmappable object type: "
					+ columnValue.getClass());
		}
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}