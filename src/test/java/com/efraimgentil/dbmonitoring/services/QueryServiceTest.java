package com.efraimgentil.dbmonitoring.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.models.ConnectionInfo;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;

import static org.mockito.Mockito.*;

public class QueryServiceTest {
	
	private QueryService queryService;
	
	@Mock private MonitorInfo monitorInfo;
	@Mock private ConnectionPool connectionPool;
	@Mock private Connection connection;
	@Mock private Statement statement;
	
	@BeforeMethod
	public void setupMethods(){
		MockitoAnnotations.initMocks(this);
		queryService = new QueryService();
		queryService.setConnectionPool( connectionPool );
	}
	
	@Test(description = "Should get the connection and executeQuery from the monitorInfo" , groups = {"success"})
	public void shouldGetTheConnectionAndExecuteTheQueryFromTheMonitorInfo() throws SQLException, ConnectionNotFound{
		
		when( connectionPool.getConnection( any(ConnectionInfo.class) ) ).thenReturn( connection );
		when( connection.createStatement() ).thenReturn( statement );
		
		queryService.executeQuery( monitorInfo );
		
		verify( statement , times(1) ).executeQuery( anyString() );
	}
	
	@Test( description = "Should throw ConnectionException when couldn't find the connection for the given monitorInfo" , groups = { "failure" } , expectedExceptions = { ConnectionNotFound.class })
	public void shouldThrowConnectionExceptionWhenCouldNotFindTheConnectionForTheGivenMonitor() throws SQLException, ConnectionNotFound{
		when( connectionPool.getConnection( any(ConnectionInfo.class) ) ).thenThrow( new ConnectionNotFound("") );
		
		queryService.executeQuery( monitorInfo );
	}
	
	@Test( description = "Should throw SQLException when try to execute a invalid query" , groups = { "failure" } , expectedExceptions = { SQLException.class } )
	public void shouldThrowSqlExceptionWhenTheQueryIsInvalid() throws SQLException, ConnectionNotFound{
		when( connectionPool.getConnection( any(ConnectionInfo.class) ) ).thenReturn( connection );
		when( connection.createStatement() ).thenReturn( statement );
		when( statement.executeQuery( anyString() ) ).thenThrow( new SQLException() );
		
		queryService.executeQuery( monitorInfo );
	}
	
	@Test( description = "Should set the connection to readOnly when is not set to readOnly already" , groups = { "success" })
	public void shouldSetConnectionToReadOnlyWhenItIsNotAlreadyReadOnly() throws SQLException{
		when( connection.isReadOnly() ).thenReturn( false );
		
		queryService.setConnectionToReadOnly(connection);
		
		verify( connection , times(1) ).isReadOnly();
		verify( connection , times(1) ).setReadOnly( true );
	}
	
	@Test( description = "Should do nothing when the connection is already set to readOnly" , groups = { "success" } )
	public void shouldDoNothingWHenConnectionIsAlreadySetToReadOnly() throws SQLException{
		when( connection.isReadOnly() ).thenReturn( true );
		
		queryService.setConnectionToReadOnly(connection);
		
		verify( connection , times(1) ).isReadOnly();
	}
	
}
