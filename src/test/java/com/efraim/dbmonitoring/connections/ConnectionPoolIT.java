package com.efraim.dbmonitoring.connections;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

public class ConnectionPoolIT {

	private ConnectionPool connectionPool;

	private MonitorInfo monitorInfo;

	@BeforeMethod
	public void initEachTest() {
		connectionPool = ConnectionPool.getInstance();
		monitorInfo = new MonitorInfo();
		monitorInfo.setDatabase(AvailableDatabase.H2);
		monitorInfo.setRefreshTime(5);
		monitorInfo.setHost("data/test_database");
		monitorInfo.setUser("sa");
		monitorInfo.setPassword("sa");
		
	}

	@AfterMethod
	public void afterEachTest() {
		connectionPool.disconnect();
	}

	@Test(description = "Should successfully open a H2 connection and return a token", groups = { "success" })
	public void shouldSuccessfullyOpenConnection() throws ClassNotFoundException, SQLException {
		monitorInfo = connectionPool.openConnection(monitorInfo);
		assertNotNull( monitorInfo.getConnectionToken() );
		assertSame( 32 , monitorInfo.getConnectionToken().length() );
	}
	
	@Test(description = "Should retrieve the connection in the passed token" , groups = { "success" })
	public void shouldRetriveTheConnection() throws ClassNotFoundException, SQLException, ConnectionNotFound{
		monitorInfo = connectionPool.openConnection(monitorInfo);
		Connection connection = connectionPool.getConnection( monitorInfo.getConnectionToken() );
		assertNotNull(connection);
		assertSame( true , !connection.isClosed() );
	}
	
	@Test(description = "Should throw a exception when the connection is not found for the passed token" ,groups = { "failure" },
			expectedExceptions = { ConnectionNotFound.class })
	public void shouldFailToRetrieveAConnection() throws ConnectionNotFound{
		connectionPool.getConnection("!!ANYTHING??");
	}

	@Test(description = "Should try and fail to open a POSTGRES connection", groups = { "failure" }, expectedExceptions = { SQLException.class })
	public void tryAndFailOpenConnection() throws ClassNotFoundException,
			SQLException {
		monitorInfo.setDatabase(AvailableDatabase.POSTGRES);
		connectionPool.openConnection(monitorInfo);
	}

}
