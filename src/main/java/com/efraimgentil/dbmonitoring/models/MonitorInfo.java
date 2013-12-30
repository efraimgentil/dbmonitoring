package com.efraimgentil.dbmonitoring.models;

import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseDeserializer;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseSerializer;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;

/**
 * 
 * @author Efraim Gentil
 * @email efraim.gentil@gmail.com
 * @date Dec 13, 2013
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorInfo {
	
	@JsonIgnore
	private static final int CONNECTION_TOKEN = 0;
	@JsonIgnore
	private static final int MONITOR_TOKEN = 1;
	
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
	
	@JsonProperty("query")
	private String query;
	
	@JsonProperty("refreshTime")
	private Integer refreshTime;
	
	@JsonProperty("token")
	private String token;
	
	@JsonIgnore
	private Calendar lastAccess;
	
	//See get and set
	@JsonProperty("action")
	private String action;
	
	public MonitorInfo() {	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MonitorInfo [database=").append(database)
				.append(", host=").append(host).append(", user=").append(user)
				.append(", password=").append(password)
				.append(", monitorTitle=").append(monitorTitle)
				.append(", query=").append(query).append(", refreshTime=")
				.append(refreshTime).append(", token=").append(token)
				.append(", lastAccess=").append(lastAccess).append(", action=")
				.append(action).append("]");
		return builder.toString();
	}

	protected String verifyAndReturnTokenPart(int part) throws NoMonitorTokenException, WrongTokenFormatException{
		if(token == null)
			throw new NoMonitorTokenException();
		String[] parts = token.split("-");
		if(parts.length <= 1)
			throw new WrongTokenFormatException();
		return parts[part];
	}
	
	@JsonIgnore
	public String getConnectionToken() throws NoMonitorTokenException, WrongTokenFormatException{
		return verifyAndReturnTokenPart( CONNECTION_TOKEN );
	}
	
	@JsonIgnore
	public String getMonitorToken() throws NoMonitorTokenException, WrongTokenFormatException{
		return verifyAndReturnTokenPart( MONITOR_TOKEN );
	}
	
	@JsonIgnore
	public void setConnectionToken( String token ) throws NoMonitorTokenException, WrongTokenFormatException{
		String monitorToken= verifyAndReturnTokenPart( MONITOR_TOKEN );
		setToken( token + "-" +  monitorToken);
	}
	
	@JsonIgnore
	public void setMonitorToken( String token ) throws NoMonitorTokenException, WrongTokenFormatException{
		String connectionToken =  verifyAndReturnTokenPart( CONNECTION_TOKEN );
		setToken( connectionToken + "-" +  token);
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}