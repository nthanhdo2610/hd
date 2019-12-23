package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;

public class ConfigContractTypeBackgroundUpdate implements HDPayload {

    private int id;
    private String contractType;
    private String contractName;
    private String backgroupImageLink;

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

    @Override
    public void validatePayload() {
//name type id
        if (id <= 0)
            throw new BadRequestException(1106, "invalid id");
        if (HDUtil.isNullOrEmpty(contractType))
            throw new BadRequestException(1227, "empty contract type");
        if (HDUtil.isNullOrEmpty(contractName))
            throw new BadRequestException(1228, "empty contract name");
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                ", contractType='" + contractType + '\'' +
                ", contractName='" + contractName + '\'' +
                ", backgroupImageLink='" + backgroupImageLink;
    }
}
