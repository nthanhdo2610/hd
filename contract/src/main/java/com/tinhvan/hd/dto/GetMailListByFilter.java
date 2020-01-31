package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class GetMailListByFilter implements HDPayload {

    private List<MailFilter> filters;

    public List<MailFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<MailFilter> filters) {
        this.filters = filters;
    }

    @Override
    public void validatePayload() {

    }
}
