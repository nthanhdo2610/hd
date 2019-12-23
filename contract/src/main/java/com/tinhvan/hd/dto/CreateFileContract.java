package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class CreateFileContract implements HDPayload {

    private String urlFile;

    private ContractInfo contract;

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public ContractInfo getContract() {
        return contract;
    }

    public void setContract(ContractInfo contract) {
        this.contract = contract;
    }

    @Override
    public void validatePayload() {

    }
}
