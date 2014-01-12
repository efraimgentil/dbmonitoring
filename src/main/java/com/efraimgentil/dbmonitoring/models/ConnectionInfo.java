package com.efraimgentil.dbmonitoring.models;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseDeserializer;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionInfo {
	
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
	
	@JsonIgnore
	private String connectionToken;
	
	public String getStringForToken(){
		return host+user+password;
	}

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

	public String getConnectionToken() {
		return connectionToken;
	}

	public void setConnectionToken(String connectionToken) {
		this.connectionToken = connectionToken;
	}
	
}