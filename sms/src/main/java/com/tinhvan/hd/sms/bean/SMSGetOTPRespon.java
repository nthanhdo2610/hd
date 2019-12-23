package com.tinhvan.hd.sms.bean;

public class SMSGetOTPRespon {

    private int otpExpired;
    private int otpResend;

    public SMSGetOTPRespon(int otpExpired, int otpResend) {
        this.otpExpired = otpExpired;
        this.otpResend = otpResend;
    }

    public int getOtpExpired() {
        return otpExpired;
    }

    public void setOtpExpired(int otpExpired) {
        this.otpExpired = otpExpired;
    }

    public int getOtpResend() {
        return otpResend;
    }

    public void setOtpResend(int otpResend) {
        this.otpResend = otpResend;
    }
}
