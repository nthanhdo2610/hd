package com.tinhvan.hd.promotion.payload;

public class FilterDTO {
    private String key;
    private String compare;
    private String value;

    public FilterDTO(String key, String compare, String value) {
        this.key = key;
        this.compare = compare;
        this.value = value;
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
