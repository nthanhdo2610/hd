package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.utils.BaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdjustmentFileRequest implements BasePayload {
    @JsonProperty("contractId")
    private String contractId;
    @JsonProperty("fileType")
    private String fileType;
    @JsonProperty("fileTemplate")
    public String fileTemplate;
    @JsonProperty("fileBackground")
    public String fileBackground;
    @JsonProperty("adjConfirmDtoList")
    List<AdjConfirmDto> adjConfirmDtoList;
    @JsonProperty("adjustmentFrom")
    AdjustmentFrom adjustmentFrom;
    @JsonProperty("fileOlds")
    List<String> fileOlds;

    @Override
    public void validatePayload() {
        adjustmentFrom.validatePayload();
        try {
            UUID.fromString(contractId);
        } catch (Exception e) {
            throw new BadRequestException(1106, "invalid id");
        }
        if (BaseUtil.isNullOrEmpty(fileTemplate))
            throw new BadRequestException(1119, "invalid fileTemplate");
        if(adjConfirmDtoList==null)
            adjConfirmDtoList = new ArrayList<>();
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

    public List<AdjConfirmDto> getAdjConfirmDtoList() {
        return adjConfirmDtoList;
    }

    public void setAdjConfirmDtoList(List<AdjConfirmDto> adjConfirmDtoList) {
        this.adjConfirmDtoList = adjConfirmDtoList;
    }

    public AdjustmentFrom getAdjustmentFrom() {
        return adjustmentFrom;
    }

    public void setAdjustmentFrom(AdjustmentFrom adjustmentFrom) {
        this.adjustmentFrom = adjustmentFrom;
    }

    public List<String> getFileOlds() {
        return fileOlds;
    }

    public void setFileOlds(List<String> fileOlds) {
        this.fileOlds = fileOlds;
    }
}
