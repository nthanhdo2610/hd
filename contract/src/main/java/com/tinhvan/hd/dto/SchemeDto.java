package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.Date;

public class SchemeDto implements HDPayload {
    private Long id;
    private String schemeName;
    private String schemeValue;
    private String fileLink;
    private Date createdAt;
    private String createdBy;
    private String createdByName;
    private Date modifiedAt;
    private String modifiedBy;

    public SchemeDto() {
    }

    public SchemeDto(Long id, String schemeName, String schemeValue, String fileLink, Date createdAt,
                     String createdBy, String createdByName, Date modifiedAt, String modifiedBy) {
        this.id = id;
        this.schemeName = schemeName;
        this.schemeValue = schemeValue;
        this.fileLink = fileLink;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public void validatePayload() {

    }
}
