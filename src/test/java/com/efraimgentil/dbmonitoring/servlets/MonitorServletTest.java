package com.efraimgentil.dbmonitoring.servlets;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.servlets.MonitorServlet;
import com.efraimgentil.dbmonitoring.servlets.actions.Action;
import com.efraimgentil.dbmonitoring.servlets.actions.CreateConnection;
import com.efraimgentil.dbmonitoring.servlets.actions.InitiateMonitor;
import com.efraimgentil.dbmonitoring.servlets.actions.InvalidAction;
import com.efraimgentil.dbmonitoring.servlets.actions.UpdateMonitor;

import static org.testng.AssertJUnit.*;

/**
 * @author efraimgentil (efraim.gentil@gmail.com)
 */
public class MonitorServletTest {
	
	MonitorServlet servlet;
	
	@BeforeMethod
	public void setUp(){
		servlet = new MonitorServlet();
	}
	
	@Test(description = "Should return initiateMonitor action when the actionIdentifier is 'INITIATE' ")
	public void shouldReturnInitiateMonitorAction(){
		String actionIdentifier = "INITIATE";

		Action action = servlet.identifyAction( actionIdentifier );
		
		assertEquals("Should have returned the InitiateMonitor action" ,  InitiateMonitor.class ,  action.getClass() );
	}
	
	@Test(description = "Should return CreateConnection action when actionIdentifier is 'OPEN_CONNECTION'")
	public void shouldReturnCreateConnectionAction(){
		String actionIdentifier = "OPEN_CONNECTION";
		
		Action action = servlet.identifyAction( actionIdentifier );
		
		assertEquals("Should have returned the CreateConnection action" ,  CreateConnection.class ,  action.getClass() );
	}
	
	@Test(description = "Should return UpdateMonitor action when actionIdentifier is 'UPDATE'")
	public void shouldReturnUpdateMonitorAction(){
		String actionIdentifier = "UPDATE";
		
		Action action = servlet.identifyAction( actionIdentifier );
		
		assertEquals("Should have returned the UpdateMonitor action" ,  UpdateMonitor.class ,  action.getClass() );
	}
	
	@Test(description = "Should return InvalidAction when actionIdentifier is not 'UPDATE'/'INITIATE'/'OPEN_CONNECTION'")
	public void shouldReturnInvalidAction(){
		String actionIdentifier = "ANITHING_DIFERENT";
		
		Action action = servlet.identifyAction( actionIdentifier );
		
		assertEquals("Should have returned the InvalidAction action" ,  InvalidAction.class ,  action.getClass() );
	}
	
}
