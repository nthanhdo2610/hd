package com.tinhvan.hd.filehandler.payload;

public class Base64Request {
    private String data;

    public Base64Request(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
