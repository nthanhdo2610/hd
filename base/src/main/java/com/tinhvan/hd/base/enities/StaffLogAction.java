package com.tinhvan.hd.base.enities;

import com.tinhvan.hd.base.HDPayload;

import java.util.Date;
import java.util.UUID;

public class StaffLogAction implements HDPayload {

    private UUID staffId;

    private String action;

    private String para;

    private String objectName;

    private String valueOld;

    private String valueNew;

    private String device;

    private String type;

    private UUID createdBy;

    private Date createdAt;

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getValueOld() {
        return valueOld;
    }

    public void setValueOld(String valueOld) {
        this.valueOld = valueOld;
    }

    public String getValueNew() {
        return valueNew;
    }

    public void setValueNew(String valueNew) {
        this.valueNew = valueNew;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void validatePayload() {

    }

    @Override
    public String toString() {
        return "StaffLogAction{" +
                "staffId=" + staffId +
                ", action='" + action + '\'' +
                ", para='" + para + '\'' +
                ", objectName='" + objectName + '\'' +
                ", valueOld='" + valueOld + '\'' +
                ", valueNew='" + valueNew + '\'' +
                ", device='" + device + '\'' +
                ", type='" + type + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
