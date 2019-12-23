package com.tinhvan.hd.sms.invoke;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.Date;
import java.util.UUID;

public class ContractEsignedRequest implements HDPayload {

    private UUID contractUuid;
    private UUID customerUuid;
    private String eSignedPhone;
    private int isSigned;
    private String otpCode;
    private Date eSignedAt;
    private String fileType;

    public ContractEsignedRequest(UUID contractUuid, UUID customerUuid, String eSignedPhone, int isSigned, String otpCode, Date eSignedAt, String fileType) {
        this.contractUuid = contractUuid;
        this.customerUuid = customerUuid;
        this.eSignedPhone = eSignedPhone;
        this.isSigned = isSigned;
        this.otpCode = otpCode;
        this.eSignedAt = eSignedAt;
        this.fileType = fileType;
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

    public String geteSignedPhone() {
        return eSignedPhone;
    }

    public void seteSignedPhone(String eSignedPhone) {
        this.eSignedPhone = eSignedPhone;
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

    public Date geteSignedAt() {
        return eSignedAt;
    }

    public void seteSignedAt(Date eSignedAt) {
        this.eSignedAt = eSignedAt;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public void validatePayload() {

        if (HDUtil.isNullOrEmpty(contractUuid.toString()))
            throw new BadRequestException(1407, "Customer uuid is null or empty");

        if (HDUtil.isNullOrEmpty(eSignedPhone))
            throw new BadRequestException(1217, "invalid Phone");

        if (HDUtil.isNullOrEmpty(otpCode))
            throw new BadRequestException(1218, "code otp is null");
    }

    @Override
    public String toString() {
        return "ContractEsignedRequest{" +
                "contractUuid=" + contractUuid +
                ", customerUuid=" + customerUuid +
                ", eSignedPhone='" + eSignedPhone + '\'' +
                ", isSigned=" + isSigned +
                ", otpCode='" + otpCode + '\'' +
                ", eSignedAt=" + eSignedAt +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
