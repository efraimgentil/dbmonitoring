package com.efraimgentil.dbmonitoring.services;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;

@Test
public class MonitorServiceTest {
	
	private MonitorService monitorService;
	
	@Mock MonitorInfo monitorInfo;
	@Mock ConnectionPool connectionPool;
	@Mock MonitorInfoPoolService monitorInfoPoolService;
	
	@BeforeMethod
	public void initMethods() throws NoMonitorTokenException, WrongTokenFormatException{
		MockitoAnnotations.initMocks(this);
		monitorService = new MonitorService( monitorInfoPoolService , connectionPool );
		when( monitorInfo.getConnectionToken() ).thenReturn("HASH_OF_CONNECTION_TOKEN");
		when( monitorInfo.getMonitorToken() ).thenReturn("HASH_OF_MONITOR_TOKEN");
	}
	
	
	@BeforeGroups(groups = { "success" })
	public void initGroupSuccess(){
	}
	
	@Test(description = "Should receive a MonitorInfo and open a connection with success" , groups = {"success"})
	public void shouldOpenAConnection() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(MonitorInfo.class) ) ).thenReturn("anytokenhehe");
		MonitorResponse monitorResponse = monitorService.openConnection( monitorInfo );
		assertTrue( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
		assertNotNull( monitorResponse.getData().get("token") );
	}
	
	@Test(description = "Should try to open the connection but has no driver so will throw a ClassNotFound" , groups = {"failure"} )
	public void shouldTryToOpenAConnectionButWasNoDriver() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(MonitorInfo.class) ) ).thenThrow( new ClassNotFoundException() );
		MonitorResponse monitorResponse = monitorService.openConnection( monitorInfo );
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should try to open the connection with the wrong connection data" , groups = {"failure"} )
	public void shouldTryToOpenAConnectionWithTheWrongConnectionData() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(MonitorInfo.class) ) ).thenThrow( new SQLException() );
		MonitorResponse monitorResponse = monitorService.openConnection( monitorInfo );
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	
	@Test(description = "Shoul initiate the monitor using with base the MonitorInfo passed" , groups = { "success" , "initiateMonitor"  } )
	public void shouldInitiateTeMonitor() throws ConnectionNotFound, SQLException{
		Connection connection = mock(Connection.class);
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet resultSet = mock(ResultSet.class) ;
		ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
		
		when ( connectionPool.getConnection( anyString() ) ).thenReturn( connection );
		when( connection.createStatement() ).thenReturn(stmt);
		when( stmt.executeQuery( anyString() ) ).thenReturn( resultSet );
		when( resultSet.getMetaData() ).thenReturn( resultSetMetaData );
//		when( resultSetMetaData.getColumnCount() ).thenReturn( 10 );
		when( resultSet.next() ).thenReturn(false);
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor(monitorInfo);
		
		assertTrue( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should not initiate the monitor when there  problems in the query" , groups = { "failure" , "initiateMonitor" })
	public void shouldNotInitiateTheMonitorWithQuerySyntaxProblems() throws ConnectionNotFound, SQLException{
		Connection connection = mock(Connection.class);
		PreparedStatement stmt = mock(PreparedStatement.class);
		
		when ( connectionPool.getConnection( anyString() ) ).thenReturn( connection );
		when( connection.createStatement() ).thenReturn(stmt);
		when( stmt.executeQuery( anyString() ) ).thenThrow( new SQLException());
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor(monitorInfo);
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should not initiate the monitor when was not possible to find the connection for informed token" , groups = { "failure" , "initiateMonitor" })
	public void shouldNotInitiateTheMonitorWithoutAConnection() throws ConnectionNotFound{
		when ( connectionPool.getConnection( anyString() ) ).thenThrow(  new ConnectionNotFound("") );
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor(monitorInfo);
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should return even if everything goes wrong, and pass a instruction message" , groups = { "failure" , "initiateMonitor" })
	public void shouldReturnEvenIfEverythingGoesWrong() throws ConnectionNotFound{
		String errorMessage = "This is a null pointer";
		when ( connectionPool.getConnection( anyString() ) ).thenThrow(  new NullPointerException( errorMessage ) );
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor(monitorInfo);
		
		assertFalse("Should be a failure response" , monitorResponse.getSuccess() );
		assertEquals( errorMessage ,  monitorResponse.getMessage() );
	}
	
	
	
}
