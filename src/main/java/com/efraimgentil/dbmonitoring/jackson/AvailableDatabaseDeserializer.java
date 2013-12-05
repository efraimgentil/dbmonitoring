package com.efraimgentil.dbmonitoring.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 5, 2013
 */
public class AvailableDatabaseDeserializer extends JsonDeserializer<AvailableDatabase> {

	@Override
	public AvailableDatabase deserialize(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		return AvailableDatabase.getAvailableDatabase( jp.getValueAsInt() );
	}

}
