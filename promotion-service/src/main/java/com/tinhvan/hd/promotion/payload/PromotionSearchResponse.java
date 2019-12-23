package com.tinhvan.hd.promotion.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.promotion.entity.Promotion;

import java.util.List;

public class PromotionSearchResponse {
    @JsonProperty("list")
    private List<Promotion> results;

    @JsonProperty("total")
    private int total;

    public PromotionSearchResponse(List<Promotion> results, int total) {
        this.results = results;
        this.total = total;
    }

    public List<Promotion> getResults() {
        return results;
    }

    public void setResults(List<Promotion> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
