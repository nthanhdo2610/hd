package com.tinhvan.hd.base;

public class NotFoundException extends HDException  {

	private static final long serialVersionUID = -6299087960968189997L;

	public NotFoundException() {
		super();
		this.code = 404;
	}

	public NotFoundException(String message) {
		super(message);
		this.code = 404;
	}

	public NotFoundException(int code, String message) {
		super(code, message);
	}

}
