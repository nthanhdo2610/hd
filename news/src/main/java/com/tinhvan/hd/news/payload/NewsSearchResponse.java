package com.tinhvan.hd.news.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinhvan.hd.news.entity.News;

import java.util.List;

public class NewsSearchResponse {
    @JsonProperty("list")
    private List<News> results;

    @JsonProperty("total")
    private int total;

    public NewsSearchResponse(List<News> results, int total) {
        this.results = results;
        this.total = total;
    }

    public List<News> getResults() {
        return results;
    }

    public void setResults(List<News> results) {
        this.results = results;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
