package com.tinhvan.hd.filehandler.exception;

public class BadRequestException extends HandlerException {

	private static final long serialVersionUID = -4891882956159634496L;

	public BadRequestException() {
		super();
		this.code = 400;
	}

	public BadRequestException(int code) {
		super(code);
	}

	public BadRequestException(int code, String message) {
		super(code, message);
	}

	public BadRequestException(String message) {
		super(message);
		this.code = 400;
	}

}
