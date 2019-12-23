package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class ContractESignedFileRequest implements HDPayload {

    private String contractUuid;
    private String contractFileName;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(contractUuid))
            throw new BadRequestException(1406);
        try{
            UUID.fromString(contractUuid);
        } catch(Exception e) {
            throw new BadRequestException(1406);
        }
        if(HDUtil.isNullOrEmpty(contractFileName))
            throw new BadRequestException(1420);
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractFileName() {
        return contractFileName;
    }

    public void setContractFileName(String contractFileName) {
        this.contractFileName = contractFileName;
    }
}
