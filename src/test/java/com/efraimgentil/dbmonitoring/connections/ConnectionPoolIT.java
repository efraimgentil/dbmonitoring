package com.efraimgentil.dbmonitoring.connections;

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
import com.efraimgentil.dbmonitoring.models.ConnectionInfo;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

public class ConnectionPoolIT {

	private ConnectionPool connectionPool;

	private ConnectionInfo connectionInfo;

	@BeforeMethod
	public void initEachTest() {
		connectionPool = ConnectionPool.getInstance();
		connectionInfo = new ConnectionInfo();
		connectionInfo.setDatabase(AvailableDatabase.H2);
		connectionInfo.setHost("data/test_database");
		connectionInfo.setUser("sa");
		connectionInfo.setPassword("sa");
		
	}

	@AfterMethod
	public void afterEachTest() {
		connectionPool.disconnect();
	}

	@Test(description = "Should successfully open a H2 connection and return a token", groups = { "success" })
	public void shouldSuccessfullyOpenConnection() throws ClassNotFoundException, SQLException {
		connectionInfo = connectionPool.openConnection(connectionInfo);
		assertNotNull( connectionInfo.getConnectionToken() );
		assertSame( 32 , connectionInfo.getConnectionToken().length() );
	}
	
	@Test(description = "Should retrieve the connection in the passed token" , groups = { "success" })
	public void shouldRetriveTheConnection() throws ClassNotFoundException, SQLException, ConnectionNotFound{
		connectionInfo = connectionPool.openConnection(connectionInfo);
		Connection connection = connectionPool.getConnection( connectionInfo.getConnectionToken() );
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
		connectionInfo.setDatabase(AvailableDatabase.POSTGRES);
		connectionPool.openConnection(connectionInfo);
	}

}
