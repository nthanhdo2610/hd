package com.tinhvan.hd.base.exception;

import com.tinhvan.hd.base.HDException;

public class ExpectationFailedException extends HDException {

    public ExpectationFailedException() {
        super();
        this.code = 417;
    }

    public ExpectationFailedException(int code) {
        super(code);
    }

    public ExpectationFailedException(int code, String message) {
        super(code, message);
    }

    public ExpectationFailedException(String message) {
        super(message);
        this.code = 417;
    }

}
