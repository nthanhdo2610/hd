package com.tinhvan.hd.dto;

import java.util.List;

public class AdminContractResponse {

    private int total;

    private List<ContractResponse> contracts;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ContractResponse> getContracts() {
        return contracts;
    }

    public void setContracts(List<ContractResponse> contracts) {
        this.contracts = contracts;
    }
}
