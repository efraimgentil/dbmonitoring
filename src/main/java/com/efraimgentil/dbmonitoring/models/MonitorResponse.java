package com.efraimgentil.dbmonitoring.models;

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
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}