package com.efraimgentil.dbmonitoring.listeners;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Efraim Gentil
 * @email efraim.gentil@gmail.com
 * @date Dec 17, 2013
 */
public class ApplicationConfig implements ServerApplicationConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		logger.info("Get annotated endpoints");
		Set<Class<?>> results = new HashSet<Class<?>>();
		for (Class<?> clazz : scanned) {
			if (clazz.getPackage().getName().startsWith("com.efraimgentil.dbmonitoring.servlets")) {
				results.add(clazz);
			}
		}
		return results;

	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(
			Set<Class<? extends Endpoint>> arg0) {
		logger.info("Get endpoints configs");
		// TODO Auto-generated method stub
		return null;
	}

}
