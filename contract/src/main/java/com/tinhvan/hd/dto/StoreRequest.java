package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class StoreRequest implements HDPayload {

    private List<StoreDto> data;

    public List<StoreDto> getData() {
        return data;
    }

    public void setData(List<StoreDto> data) {
        this.data = data;
    }

    @Override
    public void validatePayload() {

    }
}
