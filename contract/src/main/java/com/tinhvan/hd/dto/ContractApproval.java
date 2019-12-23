package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class ContractApproval implements HDPayload {

    private String contractCode;

    private Integer isConfirm;

    private PageSearch pages;


    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Integer getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
    }

    public PageSearch getPages() {
        return pages;
    }

    public void setPages(PageSearch pages) {
        this.pages = pages;
    }


    @Override
    public void validatePayload() {

        if (pages != null && pages.getPage() < 1) {
            throw new BadRequestException(1425,"Invalid page !");
        }
    }
}
