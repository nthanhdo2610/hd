package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class StoreDto implements HDPayload {

    private String name;

    private String address;

    private String provinceCode;

    private String districtCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    public void validatePayload() {

    }
}
