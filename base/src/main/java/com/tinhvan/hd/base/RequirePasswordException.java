package com.tinhvan.hd.base;

public class RequirePasswordException extends HDException  {

	private static final long serialVersionUID = -5375105333848347697L;

    public RequirePasswordException() {
		super();
		this.code = 423;
	}

	public RequirePasswordException(String message) {
		super(message);
		this.code = 423;
	}

	public RequirePasswordException(int code, String message) {
		super(code, message);
	}

}
