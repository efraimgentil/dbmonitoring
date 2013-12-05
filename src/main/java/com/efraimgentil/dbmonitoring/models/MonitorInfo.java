package com.efraimgentil.dbmonitoring.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseDeserializer;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseSerializer;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorInfo {
	
	@JsonProperty("database")
	@JsonSerialize( using = AvailableDatabaseSerializer.class  )
	@JsonDeserialize( using= AvailableDatabaseDeserializer.class )
	private AvailableDatabase database;
	
	@JsonProperty("host")
	private String host;

	@JsonProperty("user")
	private String user;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("monitorTitle")
	private String monitorTitle;
	
	@JsonProperty("refreshTime")
	private Integer refreshTime;
	
	public MonitorInfo() {	}

	public AvailableDatabase getDatabase() {
		return database;
	}

	public void setDatabase(AvailableDatabase database) {
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
