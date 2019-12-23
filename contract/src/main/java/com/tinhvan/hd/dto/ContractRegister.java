package com.tinhvan.hd.dto;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class ContractRegister implements HDPayload {

    private String contractCode;

    private String identifyId;

    private String phoneNumber;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    @Override
    public String toString() {
        return "ContractRegister{" +
                "contractCode='" + contractCode + '\'' +
                ", identifyId='" + identifyId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractCode)){
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }
        if(HDUtil.isNullOrEmpty(identifyId)){
            throw new BadRequestException(1403,"identifyId code is null or empty !");
        }
    }
}
