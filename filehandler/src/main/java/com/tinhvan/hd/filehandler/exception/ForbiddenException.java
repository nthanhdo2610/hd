package com.tinhvan.hd.filehandler.exception;

public class ForbiddenException extends HandlerException {

	private static final long serialVersionUID = -5375105333848347697L;

    public ForbiddenException() {
		super();
		this.code = 403;
	}

	public ForbiddenException(String message) {
		super(message);
		this.code = 403;
	}

	public ForbiddenException(int code, String message) {
		super(code, message);
	}

}
