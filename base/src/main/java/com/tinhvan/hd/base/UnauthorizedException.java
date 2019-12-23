package com.tinhvan.hd.base;

public class UnauthorizedException extends HDException {

	private static final long serialVersionUID = 7654953748589170268L;

    public UnauthorizedException() {
		super();
		this.code = 401;
	}

	public UnauthorizedException(String message) {
		super(message);
		this.code = 401;
	}

	public UnauthorizedException(int code, String message) {
		super(code,message);
	}

}
