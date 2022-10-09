package br.com.app.bancostout.model;

public class UsernameAlreadyExistsException extends RuntimeException {

	public UsernameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameAlreadyExistsException(String message) {
		super(message);
	}

	
}
