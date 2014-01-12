package com.efraimgentil.dbmonitoring.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

public class QueryService {
	
	private ConnectionPool connectionPool;
	
	public ResultSet executeQuery( MonitorInfo monitorInfo ) throws SQLException, ConnectionNotFound {
		Connection conn = getConnectionPool().getConnection( monitorInfo.getConnectionToken() );
		Statement stmt = conn.createStatement();
		return stmt.executeQuery( monitorInfo.getQuery() ); 
	}

	public ConnectionPool getConnectionPool() {
		if(connectionPool == null)
			connectionPool = ConnectionPool.getInstance();
		return connectionPool;
	}

	public void setConnectionPool(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}
	
}
