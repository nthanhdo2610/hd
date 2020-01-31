package com.tinhvan.hd.fcm.firebase;


public enum NotificationParameter {
    SOUND("default"),
    COLOR("#FF0000");

    private String value;

    NotificationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
