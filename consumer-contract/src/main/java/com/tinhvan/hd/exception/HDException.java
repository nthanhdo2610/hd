package com.tinhvan.hd.exception;

public class HDException extends RuntimeException {
    private static final long serialVersionUID = 1030050526603905249L;
    protected int code;

    public HDException() {
    }

    public HDException(int code) {
        this.code = code;
    }

    public HDException(String message) {
        super(message);
    }

    public HDException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}