package com.tinhvan.hd.sms.invoke;

import java.util.UUID;

public class ContractEsignedRespon {
    private UUID contractId;
    private int fileType;
    private String fileTemplate;

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(String fileTemplate) {
        this.fileTemplate = fileTemplate;
    }
}
