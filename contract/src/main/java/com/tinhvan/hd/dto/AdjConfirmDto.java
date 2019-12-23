package com.tinhvan.hd.dto;

public class AdjConfirmDto {
    private String key;
    private String name;
    private String oldValue;
    private String newValue;

    public AdjConfirmDto() {
    }

    public AdjConfirmDto(String key, String name, String newValue) {
        this.key = key;
        this.name = name;
        this.newValue = newValue;
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
