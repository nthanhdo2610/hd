package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class ContractLogStatusRequest implements HDPayload {

    private String contractUuid;
    private Integer status;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractUuid))
            throw new BadRequestException(1406);
        try{
            UUID.fromString(contractUuid);
        } catch(Exception e) {
            throw new BadRequestException(1406);
        }
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
