package com.efraimgentil.dbmonitoring.models;

import java.util.Calendar;
import java.util.Date;

import javax.websocket.Session;

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
import com.efraimgentil.dbmonitoring.utils.StringUtils;

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

	@JsonProperty("monitorTitle")
	private String monitorTitle;
	
	@JsonProperty("connectionInfo")
	private ConnectionInfo connectionInfo;

	@JsonProperty("query")
	private String query;

	@JsonProperty("refreshTime")
	private Integer refreshTime;

	@JsonProperty("token")
	private String token;

	@JsonIgnore
	private String connectionToken;

	@JsonIgnore
	private Calendar lastAccess;

	// See get and set
	@JsonProperty("action")
	private String action;

	@JsonIgnore
	private Session session;

	public MonitorInfo() {
	}
	
	public MonitorInfo(ConnectionInfo connectionInfo){
		generateToken();
		this.connectionInfo = connectionInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(", monitorTitle=").append(monitorTitle)
				.append(", query=").append(query).append(", refreshTime=")
				.append(refreshTime).append(", token=").append(token)
				.append(", lastAccess=").append(lastAccess).append(", action=")
				.append(action).append("]");
		return builder.toString();
	}

	protected String verifyAndReturnTokenPart(int part)
			throws NoMonitorTokenException, WrongTokenFormatException {
		if (token == null)
			throw new NoMonitorTokenException();
		String[] parts = token.split("-");
		if (parts.length <= 1)
			throw new WrongTokenFormatException();
		return parts[part];
	}

	@JsonIgnore
	public String getMonitorToken() throws NoMonitorTokenException,
			WrongTokenFormatException {
		return verifyAndReturnTokenPart(MONITOR_TOKEN);
	}

	@JsonIgnore
	public void setMonitorToken(String token) throws NoMonitorTokenException,
			WrongTokenFormatException {
		String connectionToken = verifyAndReturnTokenPart(CONNECTION_TOKEN);
		setToken(connectionToken + "-" + token);
	}

	@JsonIgnore
	public void generateToken() {
		token = new StringUtils().md5(new Date().toString());
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

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getConnectionToken() {
		return connectionToken;
	}

	public void setConnectionToken(String connectionToken) {
		this.connectionToken = connectionToken;
	}

}