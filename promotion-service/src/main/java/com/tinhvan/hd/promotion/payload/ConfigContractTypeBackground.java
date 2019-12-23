package com.tinhvan.hd.promotion.payload;

import java.util.Date;
import java.util.UUID;

public class ConfigContractTypeBackground {

    
    private int id;

   
    private String contractType;

    
    private String contractName;

   
    private String backgroupImageLink;

    
    private Date createdAt;

   
    private UUID createdBy;

    
    private Date modifiedAt;

   
    private UUID modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getBackgroupImageLink() {
        return backgroupImageLink;
    }

    public void setBackgroupImageLink(String backgroupImageLink) {
        this.backgroupImageLink = backgroupImageLink;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
