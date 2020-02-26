package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.entity.Contract;

import java.util.UUID;

public class ContractSendFileRequest implements HDPayload {

    private String contractUuid;
    private String customerUuid;
    private String email;
    private int signType;

    @Override
    public void validatePayload() {
        System.out.println("contract/send_file:" + toString());
        if (signType != Contract.SIGN_TYPE.OTHER && signType != Contract.SIGN_TYPE.E_SIGN && signType != Contract.SIGN_TYPE.ADJUSTMENT) {
            throw new BadRequestException(1439);
        }
        if (HDUtil.isNullOrEmpty(contractUuid))
            throw new BadRequestException(1406);
        try {
            UUID.fromString(contractUuid);
        } catch (Exception e) {
            throw new BadRequestException(1406);
        }

        if (HDUtil.isNullOrEmpty(customerUuid))
            throw new BadRequestException(1106);
        try {
            UUID.fromString(customerUuid);
        } catch (Exception e) {
            throw new BadRequestException(1106);
        }

        if (!HDUtil.isEmail(email.toLowerCase()) || email.length() > 127)
            throw new BadRequestException(1101);

    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ContractSendFileRequest{" +
                "contractUuid='" + contractUuid + '\'' +
                ", customerUuid='" + customerUuid + '\'' +
                ", email='" + email + '\'' +
                ", signType=" + signType +
                '}';
    }
}
