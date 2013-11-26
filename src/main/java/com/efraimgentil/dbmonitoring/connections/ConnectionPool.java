package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 25, 2013
 *
 */
public class ConnectionPool {

	private static Map<String, Connection> connections = new HashMap<String, Connection>();

	public static Connection getConnection(String host) {
		return (Connection) connections.get(host);
	}

	public static void openConnection(String host, String usuario,
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

	public static void disconnect() {
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
