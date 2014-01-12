package com.efraimgentil.dbmonitoring.services;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.codehaus.jackson.JsonProcessingException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;
import com.efraimgentil.dbmonitoring.connections.exceptions.ConnectionNotFound;
import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.models.ConnectionInfo;
import com.efraimgentil.dbmonitoring.models.JsonAction;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.MonitorResponse;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;
import com.efraimgentil.dbmonitoring.services.MonitorService;
import com.efraimgentil.dbmonitoring.services.QueryService;

@Test
public class MonitorServiceTest {
	
	private MonitorService monitorService;
	
	@Mock
	private MonitorInfo monitorInfo;
	
	@Mock
	private ConnectionPool connectionPool;
	
	@Mock
	private MonitorInfoPoolService monitorInfoPoolService;
	@Mock
	private ConnectionInfo connectionInfo;
	@Mock
	private QueryService queryService;
	@Mock
	private JsonAction jsonAction;
	
	@BeforeMethod
	public void initMethods() throws NoMonitorTokenException, WrongTokenFormatException{
		MockitoAnnotations.initMocks(this);
		monitorService = new MonitorService();
		monitorService.setConnectionPool(connectionPool);
		monitorService.setMonitorInfoPoolService(monitorInfoPoolService);
		monitorService.setQueryService(queryService);
		
		when( monitorInfo.getConnectionToken() ).thenReturn("ASDFFASDASD");
		when( monitorInfo.getMonitorToken() ).thenReturn("ASDFFASDASD");
	}
	
	
	@BeforeGroups(groups = { "openConnection" })
	public void initGroupSuccess(){
		when( jsonAction.getJsonDataString() ).thenReturn( "{ \"host\" : \"\" , \"user\" : \"\" , \"password\": \"\" , \"database\" : \"\" }" );
	}
	
	@Test(description = "Should receive a MonitorInfo and open a connection with success" , groups = {"success" })
	public void shouldOpenAConnection() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(ConnectionInfo.class) ) ).thenReturn( connectionInfo );
		when( jsonAction.getJsonDataString() ).thenReturn( "{ \"host\" : \"\" , \"user\" : \"\" , \"password\": \"\" , \"database\" : \"\" }" );
		doCallRealMethod().when( monitorInfo ).generateToken();
		doCallRealMethod().when( monitorInfo ).getToken();
		
		MonitorResponse monitorResponse = monitorService.openConnection( jsonAction );
		
		assertTrue( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
		assertNotNull( monitorResponse.getData().get("token") );
		verify( connectionPool , times(1) ).openConnection( any(ConnectionInfo.class) );
		verify( monitorInfoPoolService , times(1) ).addMonitor( any(MonitorInfo.class) );
	}
	
	@Test(description = "Should try to open the connection but has no driver so will throw a ClassNotFound" , groups = {"failure"} )
	public void shouldTryToOpenAConnectionButWasNoDriver() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(ConnectionInfo.class) ) ).thenThrow( new ClassNotFoundException() );

		MonitorResponse monitorResponse = monitorService.openConnection( jsonAction );
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should try to open the connection with the wrong connection data" , groups = {"failure"} )
	public void shouldTryToOpenAConnectionWithTheWrongConnectionData() throws ClassNotFoundException, SQLException{
		when( connectionPool.openConnection( any(ConnectionInfo.class) ) ).thenThrow( new SQLException() );
		
		MonitorResponse monitorResponse = monitorService.openConnection( jsonAction );
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	
	@Test(description = "Shoul initiate the monitor using with base the MonitorInfo passed" , groups = { "success" , "initiateMonitor"  } )
	public void shouldInitiateTeMonitor() throws ConnectionNotFound, SQLException, NoMonitorTokenException, WrongTokenFormatException{
		QueryService queryService = mock(QueryService.class);
		monitorService.setQueryService(queryService);
		ResultSet resultSet = mock(ResultSet.class) ;
		ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
		MonitorInfoPoolService monitorInfoPoolService = mock(MonitorInfoPoolService.class);
		
		when( queryService.executeQuery( any(MonitorInfo.class) ) ).thenReturn( resultSet );
		when( monitorInfoPoolService.updateMonitorInfoAndGet( anyString() , any(Map.class) ) ).thenReturn( monitorInfo );
		when( resultSet.getMetaData() ).thenReturn( resultSetMetaData );
		when( resultSet.next() ).thenReturn(false);
		monitorService.setMonitorInfoPoolService(monitorInfoPoolService);
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor( jsonAction );
		
		assertTrue( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
	}
	
	@Test(description = "Should not initiate the monitor when there  problems in the query" , groups = { "failure" , "initiateMonitor" })
	public void shouldNotInitiateTheMonitorWithQuerySyntaxProblems() throws ConnectionNotFound, SQLException, NoMonitorTokenException, WrongTokenFormatException{
		SQLException exception = new SQLException();
		MonitorInfo monitorInfo = mock(MonitorInfo.class);
		when( monitorInfoPoolService.updateMonitorInfoAndGet( anyString(), any(Map.class) ) ).thenReturn( monitorInfo );
		when( queryService.executeQuery( any(MonitorInfo.class) ) ).thenThrow( exception );
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor( jsonAction );
		
		verify( monitorInfoPoolService, times(1) ).updateMonitorInfoAndGet( anyString() , any(Map.class));
		assertFalse  ( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
		assertSame( exception  , monitorResponse.getException() );
	}
	
	@Test(description = "Should not initiate the monitor when was not possible to find the connection for informed token" , groups = { "failure" , "initiateMonitor" })
	public void shouldNotInitiateTheMonitorWithoutAConnection() throws ConnectionNotFound, SQLException{
		ConnectionNotFound exception = new ConnectionNotFound("");
		when( queryService.executeQuery( any(MonitorInfo.class) ) ).thenThrow( exception );
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor( jsonAction );
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
		assertSame( exception , monitorResponse.getException() );
	}
	
	@Test(description = "Should return even if everything goes wrong, and pass a instruction message" , groups = { "failure" , "initiateMonitor" })
	public void shouldReturnEvenIfEverythingGoesWrong() throws ConnectionNotFound, SQLException{
		NullPointerException exception = new NullPointerException();
		when ( queryService.executeQuery( any(MonitorInfo.class) ) ).thenThrow(  exception );
		
		MonitorResponse monitorResponse = monitorService.initiateMonitor( jsonAction );
		
		assertFalse( monitorResponse.getSuccess() );
		assertNotNull( monitorResponse.getMessage() );
		assertSame( exception , monitorResponse.getException() );
	}
	
	@Test(description = "Should create a empty ConnectionInfo object from a jsonString " , groups = { "success" })
	public void shouldCreateAEmptyConnectionInfoFromAJsonString() throws JsonProcessingException, IOException{
		String jsonString = "{}";
		
		ConnectionInfo connectionInfo =  monitorService.createConnectionInfo( jsonString );
		
		assertNotNull(connectionInfo);
	}
	
	@Test(description = "Should create a ConnectionInfo object from a jsonString " , groups = { "success" })
	public void shouldCreateAConnectionInfoFromAJsonString() throws JsonProcessingException, IOException{
		String jsonString = "{ \"host\" : \"localhost:3336\" , \"user\" : \"username\" , \"password\" : \"anypassword\" , \"database\" : \"1\" }";
		
		ConnectionInfo connectionInfo =  monitorService.createConnectionInfo( jsonString );
		
		assertNotNull(connectionInfo);
		assertEquals( "localhost:3336", connectionInfo.getHost() );
		assertEquals( "username", connectionInfo.getUser() );
		assertEquals( "anypassword", connectionInfo.getPassword() );
		assertEquals( AvailableDatabase.POSTGRES , connectionInfo.getDatabase());
	}
	
	@Test(description = "Should create a ConnectionInfo object from a jsonString " , groups = { "failure" } , expectedExceptions = { JsonProcessingException.class })
	public void shouldThrowAExceptionWhenTryingToCreateAConnectionInfoFromAWrongFormatedJsonString() throws JsonProcessingException, IOException{
		String jsonString = "{ \"host\" : \"localhost:3336\" , \"user\" : \"username\" , \"password\" : \"anypassword\" , \"database\" , \"1\" }";
		
		ConnectionInfo connectionInfo =  monitorService.createConnectionInfo( jsonString );
	}
	
	
}
