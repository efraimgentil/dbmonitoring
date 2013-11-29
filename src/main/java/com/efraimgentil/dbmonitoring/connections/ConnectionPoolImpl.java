package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.monitor.MonitorMBean;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 25, 2013
 *
 */
public class ConnectionPoolImpl implements ConnectionPool {

	private static Map<String, Connection> connections = new HashMap<String, Connection>();
	
	private static ConnectionPoolImpl connectionPool;
	
	public static ConnectionPoolImpl getInstance(){
		if(connectionPool == null){
			connectionPool  = new ConnectionPoolImpl();
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
			String driver = "org.postgresql.Driver";
			String url = "jdbc:postgresql://" + host;
			//Class.forName(driver);
			connections.put(host,  DriverManager.getConnection(url, user, password) );
		}
	}

	public void openConnection(String host, String usuario,
			String password) throws SQLException, ClassNotFoundException {
		if (connections.get(host) == null) {
			String driver = "org.postgresql.Driver";
			String url = "jdbc:postgresql://" + host;

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, usuario,
					password);
			if (conn != null)
				connections.put(host, conn);
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
	}

	

}
