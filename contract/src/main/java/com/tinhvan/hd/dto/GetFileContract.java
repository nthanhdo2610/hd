package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.entity.Contract;

import java.util.UUID;

public class GetFileContract implements HDPayload {

    private String contractCode;

    private UUID customerUuid;

    private int signType;

    /*private ContractInfo contractInfo;

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }*/

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    @Override
    public void validatePayload() {
        if(signType==0){
            signType = Contract.SIGN_TYPE.E_SIGN;
        }
        if(HDUtil.isNullOrEmpty(contractCode)){
            throw new BadRequestException(1402,"Contract code is null or empty !");
        }

        if (customerUuid == null || HDUtil.isNullOrEmpty(customerUuid.toString())) {
            throw new BadRequestException(1407,"CustomerUuid code is null or empty !");
        }
    }

    @Override
    public String toString() {
        return "GetFileContract{" +
                "contractCode='" + contractCode + '\'' +
                ", customerUuid=" + customerUuid +
                ", signType=" + signType +
                '}';
    }
}
