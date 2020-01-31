package com.tinhvan.hd.bean;

import com.tinhvan.hd.base.HDPayload;

public class MailFilter implements HDPayload{

    private Integer type;

    private String contractType;

    private String province;

    private String district;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public void validatePayload() {

    }
}
