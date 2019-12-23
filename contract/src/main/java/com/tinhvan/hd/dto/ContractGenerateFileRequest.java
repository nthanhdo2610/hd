package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;
import java.util.UUID;

public class ContractGenerateFileRequest implements HDPayload {
    private UUID contractId;
    private String fileType;
    private List<String> fileTemplates;
    private String fileBackground;
    private ContractInfo contractInfo;

    @Override
    public void validatePayload() {

    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<String> getFileTemplates() {
        return fileTemplates;
    }

    public void setFileTemplates(List<String> fileTemplates) {
        this.fileTemplates = fileTemplates;
    }

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    public String getFileBackground() {
        return fileBackground;
    }

    public void setFileBackground(String fileBackground) {
        this.fileBackground = fileBackground;
    }
}
