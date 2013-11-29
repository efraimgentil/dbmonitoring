package com.efraim.dbmonitoring.connections;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.connections.ConnectionPoolImpl;
import com.efraimgentil.dbmonitoring.models.MonitorInfo;


public class ConnectionPoolIT {
	
	private ConnectionPoolImpl connectionPool;
	
	@BeforeTest
	public void initEachTests(){
		connectionPool = new ConnectionPoolImpl();
		MonitorInfo monitorInfo;
//		monitorInfo.
		
//		Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
	}
	
	@Test
	public void openConnection(){
		
	}
	
}
