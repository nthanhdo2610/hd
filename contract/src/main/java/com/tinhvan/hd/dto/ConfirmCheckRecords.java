package com.tinhvan.hd.dto;

import com.tinhvan.hd.entity.ContractAdjustmentInfo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ConfirmCheckRecords {

    private String contractCode;

    private String status;

    private String createBy;

    private Date createdAt;

    private boolean isChanges;

    private List<ConfigCheckRecords> config;


    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isChanges() {
        return isChanges;
    }

    public void setChanges(boolean changes) {
        isChanges = changes;
    }

    public List<ConfigCheckRecords> getConfig() {
        return config;
    }

    public void setConfig(List<ConfigCheckRecords> config) {
        this.config = config;
    }
}
