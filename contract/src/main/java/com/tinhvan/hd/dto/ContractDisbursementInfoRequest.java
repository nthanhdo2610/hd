package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class ContractDisbursementInfoRequest implements HDPayload {

    private String contractUuid;
    private String bankName;
    private String accountNumber;
    private String brandName;
    private String accountName;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractUuid))
            throw new BadRequestException(1406);
        try{
            UUID.fromString(contractUuid);
        } catch(Exception e) {
            throw new BadRequestException(1406);
        }
        if(!HDUtil.isNullOrEmpty(bankName)&&bankName.length()>255)
            throw new BadRequestException(1413);
        if(!HDUtil.isNullOrEmpty(accountNumber)&&accountNumber.length()>255)
            throw new BadRequestException(1414);
        if(!HDUtil.isNullOrEmpty(brandName)&&brandName.length()>255)
            throw new BadRequestException(1415);
        if(!HDUtil.isNullOrEmpty(accountName)&&accountName.length()>255)
            throw new BadRequestException(1416);

    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
