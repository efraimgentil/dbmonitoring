package com.efraimgentil.dbmonitoring.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 28, 2013
 */
public interface ConnectionPool {
	
	public abstract Connection getConnection(String host);

	public abstract void openConnection( MonitorInfo monitorInfo ) throws SQLException, ClassNotFoundException;
	
	public abstract void disconnect();
	
}
