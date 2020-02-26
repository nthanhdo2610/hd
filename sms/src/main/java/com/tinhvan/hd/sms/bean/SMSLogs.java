package com.tinhvan.hd.sms.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
