package com.tinhvan.hd.dto;

import java.util.List;

public class DetailAdjustmentInfoMobile {

    private  String contractNumber;

    private  String phoneNumber;

    private String contractUuid;

    private List<ConfigCheckRecordsMobile> configs;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public List<ConfigCheckRecordsMobile> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigCheckRecordsMobile> configs) {
        this.configs = configs;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
