package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class BankRequest implements HDPayload {

    private List<BankDTO> data;

    public List<BankDTO> getData() {
        return data;
    }

    public void setData(List<BankDTO> data) {
        this.data = data;
    }

    @Override
    public void validatePayload() {

    }
}
