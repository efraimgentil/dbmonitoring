package com.efraimgentil.dbmonitoring.services;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;
import com.efraimgentil.dbmonitoring.models.exceptions.NoMonitorTokenException;
import com.efraimgentil.dbmonitoring.models.exceptions.WrongTokenFormatException;
import com.efraimgentil.dbmonitoring.services.MonitorInfoPoolService;

import static org.testng.AssertJUnit.*;

@Test
public class MonitorInfoPoolServiceTest {
	
	private MonitorInfoPoolService monitorInfoPoolService;
	
	@BeforeMethod
	public void setupMethods(){
		monitorInfoPoolService = MonitorInfoPoolService.getInstance();
	}
	
	@Test( description = "Should always return the same instance with the call of getInstance -- (Singleton)" , groups = { "success" })
	public void shouldReturnTheSameInstanceEveryTime(){
		MonitorInfoPoolService instance1 = MonitorInfoPoolService.getInstance();
		MonitorInfoPoolService instance2 = MonitorInfoPoolService.getInstance();
		assertTrue(  instance1 == instance2 );
	}
	
	@Test( description = "Should add monitor with the map key with the value of the monitorInfo token attribute" , groups = { "success" } )
	public void shouldPutTheMonitorInfoInTheMap(){
		MonitorInfo monitorInfo = new MonitorInfo();
		monitorInfo.setToken( "anyToken" );
		
		monitorInfoPoolService.addMonitor( monitorInfo );
		
		assertNotNull(  monitorInfoPoolService.getMonitor( monitorInfo.getToken() ) );
	}
	
	@Test( description = "" , groups = { "success" })
	public void shouldReplaceTheMonitorInfoInTheMapWithThePassedMonitor() throws NoMonitorTokenException, WrongTokenFormatException{
		MonitorInfo monitorInfo = new MonitorInfo();
		monitorInfo.setToken( "any-Token" );
		monitorInfoPoolService.addMonitor( monitorInfo );
		
		MonitorInfo monitorInfo2 = new MonitorInfo();
		monitorInfo2.setToken( "any-Token" );
		monitorInfo2.setQuery( "SELECT * FROM Nothing" );
		
		monitorInfo2 = monitorInfoPoolService.updateMappedMonitorInfo( monitorInfo2 );
		
		
		MonitorInfo monitorInfoFromPool = monitorInfoPoolService.getMonitor( monitorInfo2 .getToken() );
		
		assertSame( monitorInfoFromPool.getQuery() ,  monitorInfo2 .getQuery() );
	}
	
}
