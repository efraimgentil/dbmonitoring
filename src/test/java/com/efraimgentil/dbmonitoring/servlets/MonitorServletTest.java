package com.efraimgentil.dbmonitoring.servlets;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.type.TypeReference;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.services.MonitorService;

@Test
public class MonitorServletTest {

	String json = "{ \"action\" : \"donothing\" , \"outroParam\" : \"TESTEE IEE\" }";
//	
	
	@Test
	public void teste1() throws JsonProcessingException, IOException{
		
		ObjectMapper mapper = new ObjectMapper();
		MonitorService monitorService = new MonitorService();
		
		Map<String, Object> data = mapper.readValue( json , new TypeReference<Map<String, Object>>() {} );
		String action = (String) data.get("action");
		System.out.println( action );
	}
	
}
