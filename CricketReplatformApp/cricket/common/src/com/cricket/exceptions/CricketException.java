package com.cricket.exceptions;

public class CricketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String message = null;

	public CricketException() {
		super();
	}

	public CricketException(String message) {
		super(message);
		this.message = message;
	}

	public CricketException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
