package com.RWI.Nidhi.exception;

public class AccountNotFoundException extends RuntimeException{
	public AccountNotFoundException() {
		super();
	}

	public AccountNotFoundException(String message) {
		super(message);
	}

}
