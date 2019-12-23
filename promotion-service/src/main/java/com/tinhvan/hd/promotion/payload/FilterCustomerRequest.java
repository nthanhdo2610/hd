package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.HDPayload;

public class FilterCustomerRequest implements HDPayload {

    private int id;

    private String key;

    private String compare;

    private String value;

    @Override
    public void validatePayload() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
