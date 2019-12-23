package com.tinhvan.hd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.entity.ContractAdjustmentUploadFile;

import java.util.List;

public class AdjustmentUploadFileSearchResponse {

    @JsonProperty("list")
    private List<ContractAdjustmentUploadFile> results;

    @JsonProperty("total")
    private int total;

    public AdjustmentUploadFileSearchResponse(List<ContractAdjustmentUploadFile> results, int total) {
        this.results = results;
        this.total = total;
    }

    public List<ContractAdjustmentUploadFile> getResults() {
        return results;
    }

    public void setResults(List<ContractAdjustmentUploadFile> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
