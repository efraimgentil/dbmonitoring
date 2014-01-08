package com.efraimgentil.dbmonitoring.models;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseDeserializer;
import com.efraimgentil.dbmonitoring.jackson.AvailableDatabaseSerializer;

public class ConnectionInfo {
	
	@JsonProperty("database")
	@JsonSerialize( using = AvailableDatabaseSerializer.class  )
	@JsonDeserialize( using= AvailableDatabaseDeserializer.class )
	private AvailableDatabase database;
	
	@JsonProperty("host")
	private String host;

	@JsonProperty("user")
	private String user;
	
	@JsonProperty("password")
	private String password;
	
}
