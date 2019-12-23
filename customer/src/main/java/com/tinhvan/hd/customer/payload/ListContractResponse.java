package com.tinhvan.hd.customer.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListContractResponse {
    private List<ContractResponse> data;

    public List<ContractResponse> getData() {
        return data;
    }

    public void setData(List<ContractResponse> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListContractResponse{" +
                "data=" + data +
                '}';
    }
}
