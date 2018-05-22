package System;

public class NoParentException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoParentException(String errMsg) {
		super(errMsg);
	}
}