package com.efraimgentil.dbmonitoring.models;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAction {
	
	@JsonProperty("action")
	private String action;
	
	@JsonProperty("token")
	private String token;
	
	@JsonIgnore
	private String jsonDataString;
	
	@JsonIgnore
	private Map<String, Object> jsonDataMap;
	
	@JsonIgnore
	public String getStringValue(String key){
		return (String) jsonDataMap.get(key);
	}
	
	@JsonIgnore
	public Object getValue(String key){
		return jsonDataMap.get(key);
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getJsonDataString() {
		return jsonDataString;
	}

	public void setJsonDataString(String jsonDataString) throws JsonParseException, JsonMappingException, IOException {
		this.jsonDataString = jsonDataString;
		if(jsonDataString != null){
			ObjectMapper mapper = new ObjectMapper();
			jsonDataMap = mapper.readValue( jsonDataString , new TypeReference<Map<String, Object>>() {} );
		}
	}

	public Map<String, Object> getJsonDataMap() {
		return jsonDataMap;
	}

	public void setJsonDataMap(Map<String, Object> jsonDataMap) {
		this.jsonDataMap = jsonDataMap;
	}
	
	
	
}
