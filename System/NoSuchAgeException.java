
package System;

public class NoSuchAgeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoSuchAgeException(String errMsg) {
		super(errMsg);
	}
}
