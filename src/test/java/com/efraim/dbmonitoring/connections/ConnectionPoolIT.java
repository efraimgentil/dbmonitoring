package com.efraim.dbmonitoring.connections;

import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;


public class ConnectionPoolIT {
	
	private ConnectionPool connectionPool;
	
	private MonitorInfo monitorInfo;
	
	@BeforeMethod
	public void initEachTests(){
		connectionPool = ConnectionPool.getInstance();
		connectionPool.disconnect();
		monitorInfo = new MonitorInfo();
		monitorInfo.setDatabase( AvailableDatabase.H2 );
		monitorInfo.setRefreshTime(5);
		monitorInfo.setHost("data/test_database");
		monitorInfo.setUser("sa");
		monitorInfo.setPassword("sa");
	}
	
	@Test(description = "Should successfully open a H2 connection" , groups = { "success" }  )
	public void openConnection() throws ClassNotFoundException, SQLException{
		connectionPool.openConnection(monitorInfo);
	}
	
	@Test(description = "Should try and fail to open a POSTGRES connection"
			, groups = { "failure" }
			, expectedExceptions = { SQLException.class })
	public void tryAndFailOpenConnection() throws ClassNotFoundException, SQLException {
		monitorInfo.setDatabase( AvailableDatabase.POSTGRES );
		connectionPool.openConnection(monitorInfo);
	}
	
}
