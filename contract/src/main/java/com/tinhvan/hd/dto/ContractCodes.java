package com.tinhvan.hd.dto;

import java.util.List;

public class ContractCodes {

    private List<String> contractCodes;

    public List<String> getContractCodes() {
        return contractCodes;
    }

    public void setContractCodes(List<String> contractCodes) {
        this.contractCodes = contractCodes;
    }

    @Override
    public String toString() {
        return "ContractCodes{" +
                "contractCodes=" + contractCodes +
                '}';
    }
}
