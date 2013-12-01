package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.monitor.MonitorMBean;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

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
	
	public Connection getConnection(String host) {
		return (Connection) connections.get(host);
	}
	
	public void openConnection(MonitorInfo monitorInfo) throws SQLException, ClassNotFoundException {
		String host = monitorInfo.getHost();
		if (connections.get( host  ) == null) {
			String user = monitorInfo.getUser();
			String password = monitorInfo.getPassword();
			AvailableDatabase availableDatabase = monitorInfo.getDatabase();
//			String driver = "org.postgresql.Driver";
			String url = availableDatabase.getConnectionUrl() + host;
			//Class.forName(driver);
			connections.put(host,  DriverManager.getConnection(url, user, password) );
		}
	}

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
