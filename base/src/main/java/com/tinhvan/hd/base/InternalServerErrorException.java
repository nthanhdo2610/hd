package com.tinhvan.hd.base;

public class InternalServerErrorException extends HDException  {

	private static final long serialVersionUID = -4214553476148151054L;

    public InternalServerErrorException() {
		super();
		this.code = 500;
	}

	public InternalServerErrorException(String message) {
		super(message);
		this.code = 500;
	}

	public InternalServerErrorException(int code, String message) {
		super(code, message);
	}

}
