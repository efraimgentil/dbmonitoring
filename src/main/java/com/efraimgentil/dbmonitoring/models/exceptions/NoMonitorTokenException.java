package com.efraimgentil.dbmonitoring.models.exceptions;

/**
 * 
 * @author Efraim Gentil
 * @email efraim.gentil@gmail.com
 * @date Dec 13, 2013
 *
 */
public class NoMonitorTokenException extends Exception {

	private static final long serialVersionUID = -7276323153991022771L;
	
	public NoMonitorTokenException() { }
	
	public NoMonitorTokenException(String message) { super(message); }
	
	@Override
	public String getMessage() {
		return "The token for monitor was not found Error:" + super.getMessage();
	}
	
}
