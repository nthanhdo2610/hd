package com.tinhvan.hd.bean;

public class ConfigContractTypeBackgroundKindOffer {
    private String contractType = "";
    private String contractName = "";

    public ConfigContractTypeBackgroundKindOffer() {
    }

    public ConfigContractTypeBackgroundKindOffer(String contractType, String contractName) {
        this.contractType = contractType;
        this.contractName = contractName;
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
}
