package com.tinhvan.hd.dto;

import java.util.List;

public class ConfigCheckRecordsAndStatus {

    private boolean isChanges;

    private CheckRecords config;

    public boolean isChanges() {
        return isChanges;
    }

    public void setChanges(boolean changes) {
        isChanges = changes;
    }

    public CheckRecords getConfig() {
        return config;
    }

    public void setConfig(CheckRecords config) {
        this.config = config;
    }
}
