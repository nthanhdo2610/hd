package com.tinhvan.hd.bean;

public class ConfigStaffCheckTimeRegisterEsignRespone {
    private int result;
    private String esignFrom;
    private String esignTo;

    public ConfigStaffCheckTimeRegisterEsignRespone() {
    }

    public ConfigStaffCheckTimeRegisterEsignRespone(int result, String esignFrom, String esignTo) {
        this.result = result;
        this.esignFrom = esignFrom;
        this.esignTo = esignTo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getEsignFrom() {
        return esignFrom;
    }

    public void setEsignFrom(String esignFrom) {
        this.esignFrom = esignFrom;
    }

    public String getEsignTo() {
        return esignTo;
    }

    public void setEsignTo(String esignTo) {
        this.esignTo = esignTo;
    }

    @Override
    public String toString() {
        return "ConfigStaffCheckTimeRegisterEsignRespone{" +
                "result=" + result +
                ", esignFrom='" + esignFrom + '\'' +
                ", esignTo='" + esignTo + '\'' +
                '}';
    }
}
