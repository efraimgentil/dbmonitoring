package com.efraimgentil.dbmonitoring.models;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public class MonitorResponse {
	
	private Boolean success;
	
	private String message;
	
	private String data;
	
	public MonitorResponse() {	}
	
	public MonitorResponse(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public MonitorResponse(Boolean success, String message, String data) {
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}