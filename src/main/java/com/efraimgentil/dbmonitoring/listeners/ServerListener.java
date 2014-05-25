package com.efraimgentil.dbmonitoring.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.efraimgentil.dbmonitoring.connections.ConnectionPool;

/**
 * 
 * @author Efraim Gentil
 * @date Nov 25, 2013
 *
 */
public class ServerListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ServerListener.class);
	
	/**
	 * Disconect all open connections on the pool
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("Cleaning connection pool");
		clearConnection ( ConnectionPool.getInstance() );
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Initializing servlet context");
	}
	
	protected void clearConnection(ConnectionPool pool){
		pool.disconnect();
	}

}
