package com.efraimgentil.dbmonitoring.models.exceptions;

/**
 *
 * @author Efraim Gentil
 * @email efraim.gentil@gmail.com
 * @date Dec 13, 2013
 *
 */
public class WrongTokenFormatException extends Exception {

	private static final long serialVersionUID = -273331023146569957L;
	
	public WrongTokenFormatException() {	}
	
	public WrongTokenFormatException(String message) { super(message);	}
	
}
