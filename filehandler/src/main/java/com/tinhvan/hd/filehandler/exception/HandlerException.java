package com.tinhvan.hd.filehandler.exception;


public class HandlerException extends RuntimeException  {

	private static final long serialVersionUID = 1030050526603905249L;
	protected int code;

    public HandlerException() {
		super();
	}

	public HandlerException(int code) {
		super();
		this.code = code;
	}

	public HandlerException(String message) {
		super(message);
	}

	public HandlerException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
