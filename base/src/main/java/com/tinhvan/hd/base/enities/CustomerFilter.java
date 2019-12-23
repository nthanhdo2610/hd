package com.tinhvan.hd.base.enities;

import com.tinhvan.hd.base.HDPayload;

public class CustomerFilter implements HDPayload {

    private Integer ageFrom;
    private Integer ageTo;
    private Short gender;
    private Integer province;
    private Integer district;

    @Override
    public void validatePayload() {
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
    }

    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }
}
