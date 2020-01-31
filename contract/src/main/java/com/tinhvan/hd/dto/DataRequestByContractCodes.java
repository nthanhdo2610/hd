package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class DataRequestByContractCodes implements HDPayload {

    private List<CustomerIdsByContractCode> data;

    public List<CustomerIdsByContractCode> getData() {
        return data;
    }

    public void setData(List<CustomerIdsByContractCode> data) {
        this.data = data;
    }

    @Override
    public void validatePayload() {

    }
}
