package com.tinhvan.hd.sms.invoke;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.UUID;

public class ContractEsignedFileRequest implements HDPayload {

    private UUID contractUuid;
    private String contractFileName;

    public ContractEsignedFileRequest(UUID contractUuid, String contractFileName) {
        this.contractUuid = contractUuid;
        this.contractFileName = contractFileName;
    }

    @Override
    public void validatePayload() {
        if (HDUtil.isNullOrEmpty(contractUuid.toString()))
            throw new BadRequestException(1200, "uuid is null");

        if (HDUtil.isNullOrEmpty(contractFileName))
            throw new BadRequestException(1229, "empty contract esigned file");
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }

    public String getContractFileName() {
        return contractFileName;
    }

    public void setContractFileName(String contractFileName) {
        this.contractFileName = contractFileName;
    }
}
