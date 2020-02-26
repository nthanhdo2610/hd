package com.tinhvan.hd.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "results"
})
public class SMSLogs {

    @JsonProperty("results")
    private List<SMSLogsResults> results;

    @JsonProperty("results")
    public List<SMSLogsResults> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<SMSLogsResults> results) {
        this.results = results;
    }
}
