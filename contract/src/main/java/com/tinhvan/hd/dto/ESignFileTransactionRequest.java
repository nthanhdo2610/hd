package com.tinhvan.hd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ESignFileTransactionRequest implements HDPayload {

    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("fileType")
    private String fileType;
    @JsonProperty("fileTemplate")
    public String fileTemplate;
    @JsonProperty("fileBackground")
    public String fileBackground;
    @JsonProperty("fileNames")
    List<String> fileNames; //1
    @JsonProperty("contractPrinting")
    public String contractPrinting;//2
    @JsonProperty("receiveAt")
    public String receiveAt;//5
    @JsonProperty("fullName")
    public String fullName;//6
    @JsonProperty("addressFamilyBookNo")
    public String addressFamilyBookNo;//7
    @JsonProperty("createdAt")
    public String createdAt;//8
    @JsonProperty("environment")
    public String environment;//10
    @JsonProperty("fileSize")
    public String fileSize;//12


    @Override
    public void validatePayload() {
        try {
            UUID.fromString(contractId);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
        if (HDUtil.isNullOrEmpty(fileTemplate))
            throw new BadRequestException(1119, "invalid fileTemplate");
        if(fileNames==null)
            fileNames = new ArrayList<>();
    }

    public String getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(String fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public String getFileBackground() {
        return fileBackground;
    }

    public void setFileBackground(String fileBackground) {
        this.fileBackground = fileBackground;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public String getContractPrinting() {
        return contractPrinting;
    }

    public void setContractPrinting(String contractPrinting) {
        this.contractPrinting = contractPrinting;
    }

    public String getReceiveAt() {
        return receiveAt;
    }

    public void setReceiveAt(String receiveAt) {
        this.receiveAt = receiveAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddressFamilyBookNo() {
        return addressFamilyBookNo;
    }

    public void setAddressFamilyBookNo(String addressFamilyBookNo) {
        this.addressFamilyBookNo = addressFamilyBookNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

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
}
