package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class ContractSearch implements HDPayload {

    private String contractCode;

    private String identifyId;

    private String phoneNumber;

    private PageSearch pages;

    private List<SortFilter> sort;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PageSearch getPages() {
        return pages;
    }

    public void setPages(PageSearch pages) {
        this.pages = pages;
    }

    public List<SortFilter> getSort() {
        return sort;
    }

    public void setSort(List<SortFilter> sort) {
        this.sort = sort;
    }

    @Override
    public void validatePayload() {

    }
}
