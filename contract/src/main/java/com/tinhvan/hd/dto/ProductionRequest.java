package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

public class ProductionRequest implements HDPayload {

    private String productionNames;

    private String type;

    public String getProductionNames() {
        return productionNames;
    }

    public void setProductionNames(String productionNames) {
        this.productionNames = productionNames;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void validatePayload() {

    }
}
