package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

	private static Map<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
	
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
	public MonitorInfo openConnection(MonitorInfo monitorInfo) throws SQLException, ClassNotFoundException {
		String connectionToken = new StringUtils().md5( monitorInfo.getHost() + monitorInfo.getUser() + monitorInfo.getPassword() );
		if (connections.get( connectionToken ) == null) {
			String user = monitorInfo.getUser();
			String password = monitorInfo.getPassword();
			AvailableDatabase availableDatabase = monitorInfo.getDatabase();
			
			Class.forName( availableDatabase.getDriver() );
			String url = availableDatabase.getConnectionUrl() + monitorInfo.getHost();
			connections.put( connectionToken , DriverManager.getConnection( url, user, password ) );
		}
		monitorInfo.setConnectionToken(connectionToken);
		return monitorInfo;
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
