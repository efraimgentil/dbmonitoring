package com.efraimgentil.dbmonitoring.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.efraimgentil.dbmonitoring.models.MonitorInfo;

@ServerEndpoint(value = "/webs/monitor")
public class WSMonitor {
	
	private Integer id;
	
	public WSMonitor() { }

	@OnOpen
	public void open(Session session) {
		
	}

	@OnClose
	public void close() {
	}

	@OnMessage
	public String handleMessage(Session session, String message) {
		MonitorInfo monitorInfo ;
		try {
			ObjectMapper mapper = new ObjectMapper();
			monitorInfo = mapper.reader(MonitorInfo.class).readValue( message  );
			System.out.println(monitorInfo);
			
			System.out.println( mapper.writeValueAsString(monitorInfo) );
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Message received: " + message;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WSMonitor other = (WSMonitor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
