package com.tinhvan.hd.base;

public class TypePayload implements HDPayload {

    private int type;

    @Override
    public void validatePayload() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
