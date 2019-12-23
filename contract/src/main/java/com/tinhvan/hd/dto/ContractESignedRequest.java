package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.entity.Contract;

import java.util.Date;
import java.util.UUID;

public class ContractESignedRequest implements HDPayload {

    private UUID contractUuid;
    private UUID customerUuid;
    private int isSigned;
    private String otpCode;
    private String eSignedPhone;
    private Date eSignedAt;

    /*private ContractInfo contractInfo;

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }*/

    @Override
    public void validatePayload() {
        if(isSigned==0){
            isSigned = Contract.SIGN_TYPE.E_SIGN;
        }
        if(!HDUtil.isNullOrEmpty(eSignedPhone)){
            if(!HDUtil.isPhoneNumber(eSignedPhone)){
                throw new BadRequestException(1417);
            }
        }
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
    }

    public int getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(int isSigned) {
        this.isSigned = isSigned;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String geteSignedPhone() {
        return eSignedPhone;
    }

    public void seteSignedPhone(String eSignedPhone) {
        this.eSignedPhone = eSignedPhone;
    }

    public Date geteSignedAt() {
        return eSignedAt;
    }

    public void seteSignedAt(Date eSignedAt) {
        this.eSignedAt = eSignedAt;
    }

    @Override
    public String toString() {
        return "ContractESignedRequest{" +
                "contractUuid=" + contractUuid +
                ", customerUuid=" + customerUuid +
                ", isSigned=" + isSigned +
                ", otpCode='" + otpCode + '\'' +
                ", eSignedPhone='" + eSignedPhone + '\'' +
                ", eSignedAt=" + eSignedAt +
                '}';
    }
}
