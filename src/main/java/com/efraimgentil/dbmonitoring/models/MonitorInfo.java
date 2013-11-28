package com.efraimgentil.dbmonitoring.models;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public class MonitorInfo {
	
	private String database;
	
	private String host;

	private String user;
	
	private String password;
	
	private String monitorTitle;
	
	private Integer refreshTime;
	
	public MonitorInfo() {	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMonitorTitle() {
		return monitorTitle;
	}

	public void setMonitorTitle(String monitorTitle) {
		this.monitorTitle = monitorTitle;
	}

	public Integer getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}
	
	
}
