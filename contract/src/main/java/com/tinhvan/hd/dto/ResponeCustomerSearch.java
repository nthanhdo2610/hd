package com.tinhvan.hd.dto;

import java.util.List;

public class ResponeCustomerSearch {

    private List<ContractCodeAndStatus> data;

    public List<ContractCodeAndStatus> getData() {
        return data;
    }

    public void setData(List<ContractCodeAndStatus> data) {
        this.data = data;
    }
}
