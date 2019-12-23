package com.tinhvan.hd.filehandler.exception;

public class ConflictException extends HandlerException {

    private static final long serialVersionUID = 3058793351139077539L;

	public ConflictException() {
		super();
		this.code = 409;
	}

	public ConflictException(String message) {
		super(message);
		this.code = 409;
	}

	public ConflictException(int code, String message) {
		super(code, message);
	}

}
