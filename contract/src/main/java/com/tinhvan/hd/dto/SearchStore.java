package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class SearchStore implements HDPayload {

    private String provinceCode;

    private String districtCode;

    private PageSearch pages;

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

    public PageSearch getPages() {
        return pages;
    }

    public void setPages(PageSearch pages) {
        this.pages = pages;
    }

    @Override
    public void validatePayload() {

    }
}
