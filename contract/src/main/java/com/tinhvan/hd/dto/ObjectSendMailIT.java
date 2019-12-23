package com.tinhvan.hd.dto;

import java.util.Date;

public class ObjectSendMailIT {

    private String contractCode;

    private String key;

    private String name;

    private String valueOld;

    private String valueAdjustmentInfo;

    private String staffName;

    private Date confirmDate;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
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

    public String getValueOld() {
        return valueOld;
    }

    public void setValueOld(String valueOld) {
        this.valueOld = valueOld;
    }

    public String getValueAdjustmentInfo() {
        return valueAdjustmentInfo;
    }

    public void setValueAdjustmentInfo(String valueAdjustmentInfo) {
        this.valueAdjustmentInfo = valueAdjustmentInfo;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public ObjectSendMailIT(String contractCode, String key, String name, String valueAdjustmentInfo, String staffName, Date confirmDate) {
        this.contractCode = contractCode;
        this.key = key;
        this.name = name;
        this.valueAdjustmentInfo = valueAdjustmentInfo;
        this.staffName = staffName;
        this.confirmDate = confirmDate;
    }

    public ObjectSendMailIT(String contractCode, String key, String name, String valueOld, String valueAdjustmentInfo, String staffName, Date confirmDate) {
        this.contractCode = contractCode;
        this.key = key;
        this.name = name;
        this.valueOld = valueOld;
        this.valueAdjustmentInfo = valueAdjustmentInfo;
        this.staffName = staffName;
        this.confirmDate = confirmDate;
    }
}
