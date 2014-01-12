package com.efraimgentil.dbmonitoring.services;

import java.util.HashMap;
import java.util.Map;

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
		String token = "tokenadsasuduashdiaus";
		String query = "SELECT * FROM Nothing";
		MonitorInfo monitorInfo = new MonitorInfo();
		monitorInfo.setToken( token );
		monitorInfoPoolService.addMonitor( monitorInfo );
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("query", query);
		monitorInfo = monitorInfoPoolService.updateMonitorInfoAndGet( token , jsonMap );
		
		assertSame( query ,  monitorInfo .getQuery() );
	}
	
}
