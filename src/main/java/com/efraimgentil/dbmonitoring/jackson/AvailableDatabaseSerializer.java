package com.efraimgentil.dbmonitoring.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.efraimgentil.dbmonitoring.constants.AvailableDatabase;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 5, 2013
 */
public class AvailableDatabaseSerializer extends JsonSerializer<AvailableDatabase> {

	@Override
	public void serialize(AvailableDatabase value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeNumber( value.getId() );
	}

}
