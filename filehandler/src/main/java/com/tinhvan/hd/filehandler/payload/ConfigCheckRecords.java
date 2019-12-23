package com.tinhvan.hd.filehandler.payload;

public class ConfigCheckRecords {

    private String key;

    private String value;

    private String valueNew;

    private String valueConfirm;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueNew() {
        return valueNew;
    }

    public void setValueNew(String valueNew) {
        this.valueNew = valueNew;
    }

    public String getValueConfirm() {
        return valueConfirm;
    }

    public void setValueConfirm(String valueConfirm) {
        this.valueConfirm = valueConfirm;
    }
}
