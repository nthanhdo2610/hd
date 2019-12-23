package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.HDPayload;

import java.util.ArrayList;
import java.util.List;

public class FilterCustomerDTO implements HDPayload {

    private List<FilterDTO> filters;

    @Override
    public void validatePayload() {
        if(filters==null)
            filters = new ArrayList<>();
    }

    public FilterCustomerDTO(List<FilterDTO> filters) {
        this.filters = filters;
    }

    public List<FilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDTO> filters) {
        this.filters = filters;
    }
}
