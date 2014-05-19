package com.efraimgentil.dbmonitoring.connections.exceptions;

/**
 * 
 * @author Efraim Gentil
 * @date Dec 13, 2013
 */
public class ConnectionNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ConnectionNotFound(String message) {
		super(message);
	}
	
	
	@Override
	public String getMessage() {
		return "The connection for this token was not found. Error: " +  super.getMessage();
	}
}
