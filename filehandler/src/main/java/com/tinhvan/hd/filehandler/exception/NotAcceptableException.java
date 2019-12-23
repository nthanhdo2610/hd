package com.tinhvan.hd.filehandler.exception;

public class NotAcceptableException extends HandlerException {

    private static final long serialVersionUID = 3058793351139077539L;

	public NotAcceptableException() {
		super();
		this.code = 406;
	}

	public NotAcceptableException(String message) {
		super(message);
		this.code = 406;
	}

	public NotAcceptableException(int code, String message) {
		super(code, message);
	}

}
