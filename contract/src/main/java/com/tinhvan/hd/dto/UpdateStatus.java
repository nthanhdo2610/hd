package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class UpdateStatus implements HDPayload {

    private String contractCode;

    private String status;

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

    @Override
    public String toString() {
        return "UpdateStatus{" +
                "contractCode='" + contractCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(contractCode)) {
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }

//        if (HDUtil.isNullOrEmpty(status)) {
//            throw new BadRequestException(1427,"Status is null or empty !");
//        }
    }
}
