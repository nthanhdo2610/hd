package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class IdentifyIds implements HDPayload {
    List<String> identifyIds;

    public List<String> getIdentifyIds() {
        return identifyIds;
    }

    public void setIdentifyIds(List<String> identifyIds) {
        this.identifyIds = identifyIds;
    }

    @Override
    public void validatePayload() {

    }
}
