package com.tinhvan.hd.dto;

import java.util.List;

public class ContractByStatusMobile {

    private String code;

    private List<ContractResponseMobile> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ContractResponseMobile> getData() {
        return data;
    }

    public void setData(List<ContractResponseMobile> data) {
        this.data = data;
    }
}
