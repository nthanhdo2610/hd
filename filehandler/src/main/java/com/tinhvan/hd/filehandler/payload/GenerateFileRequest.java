package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;

import java.util.List;
import java.util.UUID;

public class GenerateFileRequest implements BasePayload {

    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("fileType")
    private String fileType;
    @JsonProperty("fileTemplates")
    private List<FileRequest> fileTemplates;
    @JsonProperty("fileBackground")
    private String fileBackground;
    @JsonProperty("contractInfo")
    private ContractInfo contractInfo;
    @JsonProperty("fileOlds")
    List<String> fileOlds;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<FileRequest> getFileTemplates() {
        return fileTemplates;
    }

    public void setFileTemplates(List<FileRequest> fileTemplates) {
        this.fileTemplates = fileTemplates;
    }

    public String getFileBackground() {
        return fileBackground;
    }

    public void setFileBackground(String fileBackground) {
        this.fileBackground = fileBackground;
    }

    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    public List<String> getFileOlds() {
        return fileOlds;
    }

    public void setFileOlds(List<String> fileOlds) {
        this.fileOlds = fileOlds;
    }

    public GenerateFileRequest(String contractId, String fileType) {
        this.contractId = contractId;
        this.fileType = fileType;
    }

    @Override
    public void validatePayload() {
        try {
            UUID.fromString(contractId);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
        if (fileTemplates==null|| fileTemplates.isEmpty())
            throw new BadRequestException(1119, "invalid fileTemplate");
        if(contractInfo==null)
            contractInfo = new ContractInfo();
    }
}
