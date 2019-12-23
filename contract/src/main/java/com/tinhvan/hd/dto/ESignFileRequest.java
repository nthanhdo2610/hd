package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class ESignFileRequest implements HDPayload {

    private String contractId;
    private String fileType;
    public String fileTemplate;
    public String fileBackground;
    public List<MergeFilePdf> fields;


    @Override
    public void validatePayload() {

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

    public List<MergeFilePdf> getFields() {
        return fields;
    }

    public void setFields(List<MergeFilePdf> fields) {
        this.fields = fields;
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
