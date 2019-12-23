package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.List;
import java.util.UUID;

public class UpdateCheckRecords implements HDPayload {

    private String contractCode;

    private UUID createdBy;

    private UUID createdConfirmBy;

    private List<ConfigCheckRecords> config;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public List<ConfigCheckRecords> getConfig() {
        return config;
    }

    public UUID getCreatedConfirmBy() {
        return createdConfirmBy;
    }

    public void setCreatedConfirmBy(UUID createdConfirmBy) {
        this.createdConfirmBy = createdConfirmBy;
    }

    public void setConfig(List<ConfigCheckRecords> config) {
        this.config = config;
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractCode)){
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }
    }
}
