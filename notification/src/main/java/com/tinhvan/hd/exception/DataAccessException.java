package com.tinhvan.hd.exception;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = -2166651831132818707L;

	public DataAccessException() {
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}