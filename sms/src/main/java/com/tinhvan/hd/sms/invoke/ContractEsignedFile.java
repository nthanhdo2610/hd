package com.tinhvan.hd.sms.invoke;

import java.util.Date;
import java.util.UUID;

public class ContractEsignedFile {

    private Integer id;

    private UUID contractUUID;

    private String contractEsignedFile;

    private Date createdAt;

    public UUID getContractUUID() {
        return contractUUID;
    }

    public void setContractUUID(UUID contractUUID) {
        this.contractUUID = contractUUID;
    }

    public String getContractEsignedFile() {
        return contractEsignedFile;
    }

    public void setContractEsignedFile(String contractEsignedFile) {
        this.contractEsignedFile = contractEsignedFile;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
