package com.tinhvan.hd.staff.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileSizeResponse {

    @JsonProperty("maxSize")
    private int maxSize;
    @JsonProperty("type")
    private String type;

    public FileSizeResponse(int maxSize, String type) {
        this.maxSize = maxSize;
        this.type = type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
