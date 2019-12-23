package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class SchemePost implements HDPayload {
    private Long id;
    private String schemeName;
    private String schemeValue;
    private String fileLink;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeValue() {
        return schemeValue;
    }

    public void setSchemeValue(String schemeValue) {
        this.schemeValue = schemeValue;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    @Override
    public void validatePayload() {
        //validate name
        if (HDUtil.isNullOrEmpty(schemeName)) {
            throw new BadRequestException(1203, "invalid scheme name");
        }
        //validate role
        if (HDUtil.isNullOrEmpty(schemeValue)) {
            throw new BadRequestException(1204, "invalid scheme value");
        }

        if (HDUtil.isNullOrEmpty(fileLink)) {
            throw new BadRequestException(1205, "invalid file link");
        }
    }
}
