package com.mygroup.springstore.exception;

public class AuthorizationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message) {
        super(message);
    }
    
    public AuthorizationException(String message, Exception e) {
        super(message, e);
    }
}
