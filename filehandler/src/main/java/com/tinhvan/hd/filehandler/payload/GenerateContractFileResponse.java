package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GenerateContractFileResponse {
    @JsonProperty("files")
    private List<ContractFileResponse> files;
    @JsonProperty("data")
    private String data;

    public GenerateContractFileResponse() {
        super();
    }

    public GenerateContractFileResponse(List<ContractFileResponse> files, String data) {
        this.files = files;
        this.data = data;
    }

    public List<ContractFileResponse> getFiles() {
        return files;
    }

    public void setFiles(List<ContractFileResponse> files) {
        this.files = files;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
