package com.tweetcrawl.agents.utils;

/**
 * Exception used when an instance conflict is detected in the
 * {@code BBPetterson} class.
 */
public class BBPettersonException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the {@code BBPettersonException}.
	 * 
	 * @param message the message of the exception
	 */
	public BBPettersonException(String message) {
		super(message);
	}

}
