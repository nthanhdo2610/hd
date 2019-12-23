package com.tinhvan.hd.dto;

public class ContractAdjustmentDetail {

    private String key;

    private String name;

    private String value;

    private String valueConfirm;

    private String valueAdjustment;

    public ContractAdjustmentDetail(String key, String name,String valueAdjustment, String value, String valueConfirm) {
        this.key = key;
        this.name = name;
        this.valueAdjustment = valueAdjustment;
        this.value = value;
        this.valueConfirm = valueConfirm;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueConfirm() {
        return valueConfirm;
    }

    public void setValueConfirm(String valueConfirm) {
        this.valueConfirm = valueConfirm;
    }

    public String getValueAdjustment() {
        return valueAdjustment;
    }

    public void setValueAdjustment(String valueAdjustment) {
        this.valueAdjustment = valueAdjustment;
    }
}
