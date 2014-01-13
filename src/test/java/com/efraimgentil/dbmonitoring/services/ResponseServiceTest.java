package com.efraimgentil.dbmonitoring.services;

import java.util.HashMap;
import java.util.Map;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.models.MonitorResponse;

import static org.testng.AssertJUnit.*;

@Test
public class ResponseServiceTest {
	
	private ResponseService responseService;
	
	private static final String ERROR_MESSAGE = "Failure message";
	
	@BeforeMethod
	public void setupMethods(){
		MockitoAnnotations.initMocks(this);
		responseService = new ResponseService();
	}
	
	@Test( description = "Should create a failure reponse with a message" , groups = { "success" } )
	public void shouldCreateAFailureResponse(){
		MonitorResponse monitorResponse = responseService.createFailureResponse( ERROR_MESSAGE );
		
		assertNotNull( monitorResponse );
		assertFalse(  monitorResponse.getSuccess() );
		assertEquals( ERROR_MESSAGE , monitorResponse.getMessage() );
	}
	
	@Test( description = "Should create a failure response with a hashMap " , groups = { "success" } )
	public void shouldCreateAFailureResponseWithAMapOfAttributes(){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", "any Value");
		
		MonitorResponse monitorResponse = responseService.createFailureResponse( data );
		
		assertNotNull( monitorResponse );
		assertFalse(  monitorResponse.getSuccess() );
		assertEquals( data , monitorResponse.getData() );
	}
	
}
