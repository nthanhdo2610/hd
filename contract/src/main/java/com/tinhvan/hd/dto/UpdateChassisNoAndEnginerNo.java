package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class UpdateChassisNoAndEnginerNo implements HDPayload {

    private String contractCode;

    // so khung
    private String chassisNo;

    // so may
    private String engineerNo;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getEngineerNo() {
        return engineerNo;
    }

    public void setEngineerNo(String engineerNo) {
        this.engineerNo = engineerNo;
    }

    @Override
    public String toString() {
        return "UpdateChassisNoAndEnginerNo{" +
                "contractCode='" + contractCode + '\'' +
                ", chassisNo='" + chassisNo + '\'' +
                ", engineerNo='" + engineerNo + '\'' +
                '}';
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(contractCode)) {
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }

        if (HDUtil.isNullOrEmpty(chassisNo)) {
            throw new BadRequestException(1428,"ChassisNo code is null or empty !");
        }

        if (HDUtil.isNullOrEmpty(engineerNo)) {
            throw new BadRequestException(1429,"EngineerNo code is null or empty !");
        }
    }
}
