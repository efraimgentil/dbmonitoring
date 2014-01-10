package com.efraimgentil.dbmonitoring.models;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.models.MonitorResponse;

import static org.testng.AssertJUnit.*;
import static org.mockito.Mockito.*;

@Test
public class MonitorResponseTest {
	
	private MonitorResponse monitorResponse;
	
	@Mock
	private ResultSet resultSet;
	
	@Mock
	private ResultSetMetaData resultSetMetaData;
	
	@BeforeTest
	public void setupTest(){
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeMethod
	public void setupMethods() throws SQLException{
		monitorResponse  = new MonitorResponse();
		
		when( resultSet.getMetaData() ).thenReturn(resultSetMetaData);
		when( resultSetMetaData.getColumnCount() ).thenReturn( 3 );
	}
	
	@BeforeGroups(groups = { "success" })
	public void setupSuccessGroup() throws SQLException{
		when( resultSetMetaData.getColumnLabel( 1 ) ).thenReturn("label1");
		when( resultSetMetaData.getColumnLabel( 2 ) ).thenReturn("label2");
		when( resultSetMetaData.getColumnLabel( 3 ) ).thenReturn("label3");
	}
	
	@Test( description = "Given a ResultSet should create a List containing the result set Entries in a HashMap format" , groups = { "success" })
	public void givenAResultSetShouldCreateAListOfMap() throws SQLException{
		when( resultSet.next() ).thenReturn( true , true , true , false ); 
		
		monitorResponse.createRows(resultSet);
		
		verify( resultSet , times(3) ).getObject( "label1" );
		verify( resultSet , times(3) ).getObject( "label2" );
		verify( resultSet , times(3) ).getObject( "label3" );
		Object rows =  monitorResponse.getData().get("rows");
		assertNotNull( rows );
		assertTrue( rows instanceof List );
		assertSame( 3 , ((List) rows).size() );
		Map<String, Object> row = (Map<String, Object>) ((List) rows).get(0);
		assertTrue( row.containsKey("label1") );
		assertTrue( row.containsKey("label2") );
	}
	
	@Test( description = "Given a ResultSetMetaData should return a list of columnLabels of this ResultSetMetaData" , groups = { "success" } )
	public void givenAResultSetMetaDataShouldReturnAListOfColumnLabels() throws SQLException{
		
		List<String> columnLabels = monitorResponse.getColumnLabels( resultSetMetaData );
		
		assertSame( 3 , columnLabels.size() );
		assertTrue( columnLabels.contains("label1") );
		assertTrue( columnLabels.contains("label2") );
		assertTrue( columnLabels.contains("label3") );
	}
	
	@Test( description = "Given a ResultSetMetaData should return a list of columnLabels of this ResultSetMetaData" , groups = { "success" } )
	public void givenAColumnValueShouldReturnTheValue(){
		Object object = new Integer( 10 );
		
		Object wrapedValue = monitorResponse.getColumnWrapedValue(object);
		
		assertNotNull(object);
	}
	
	@Test( description = "Given a ResultSetMetaData should return a list of columnLabels of this ResultSetMetaData" ,
			groups = { "failure" } , expectedExceptions = { IllegalArgumentException.class } )
	public void givenAColumnValueWithUnmappedTypeShouldThrowIllegalArgumentException(){
		Object object = new Object();
		
		Object wrapedValue = monitorResponse.getColumnWrapedValue(object);
	}
	
	@Test( description = "Given a column value with a valid type should return true" , groups = { "success" } )
	public void givenAColumnValueShoudValidateIfIsAValidType(){
		Object columnValue = new Integer(10);
		
		Boolean isValid = monitorResponse.isValidValue(columnValue);
		
		assertTrue( isValid );
	}
	
	@Test( description = "Given a null column value should return true" , groups = { "success" } )
	public void givenANullColumnValueShoudValidateIfIsAValidType(){
		Object columnValue = null;
		
		Boolean isValid = monitorResponse.isValidValue(columnValue);
		
		assertTrue( isValid );
	}
	
	@Test( description = "Given a column value with a invalid type should return false" , groups = { "failure" } )
	public void givenAColumnValueWithInvalidTypeShouldReturnFalse(){
		Object columnValue = new Object();
		
		Boolean isValid = monitorResponse.isValidValue(columnValue);
		
		assertFalse( isValid );
	}
	
}
