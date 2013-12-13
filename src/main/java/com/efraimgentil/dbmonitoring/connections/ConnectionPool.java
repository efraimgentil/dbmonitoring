package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.utils.StringUtils;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 25, 2013
 *
 */
public class ConnectionPool {

	private static Map<String, Connection> connections = new HashMap<String, Connection>();
	
	private static ConnectionPool connectionPool;
	
	private ConnectionPool() {	}
	
	public static ConnectionPool getInstance(){
		if(connectionPool == null){
			connectionPool  = new ConnectionPool();
		}
		return connectionPool;
	}
	
	/**
	 * Returns the connection mapped in the informed token
	 * @param token
	 * @return
	 * @throws ConnectionNotFound 
	 */
	public Connection getConnection(String token) throws ConnectionNotFound {
		if(!connections.containsKey(token))
			throw new ConnectionNotFound("Has not possible to find the connection to the iformed token: " + token);
		return connections.get( token );
	}
	
	/**
	 * This method is used to open a connection and map it in a connection map,
	 * the key(token) to retrieve the connection is the host + user + password MD5
	 * @param monitorInfo
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String openConnection(MonitorInfo monitorInfo) throws SQLException, ClassNotFoundException {
		String token = new StringUtils().md5( monitorInfo.getHost() + monitorInfo.getUser() + monitorInfo.getPassword() );
		if (connections.get( token ) == null) {
			String user = monitorInfo.getUser();
			String password = monitorInfo.getPassword();
			AvailableDatabase availableDatabase = monitorInfo.getDatabase();
			
			Class.forName( availableDatabase.getDriver() );
			String url = availableDatabase.getConnectionUrl() + monitorInfo.getHost();
			connections.put( token , DriverManager.getConnection( url, user, password ) );
		}
		return token;
	}
	
	/**
	 * Close all open connections
	 */
	public void disconnect() {
		for (String key : connections.keySet()) {
			try {
				if (!((Connection) connections.get(key)).isClosed())
					((Connection) connections.get(key)).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		connections.clear();
	}

}
