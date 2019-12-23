package com.tinhvan.hd.promotion.payload;

import com.tinhvan.hd.base.HDPayload;

public class HomeRequest implements HDPayload  {
    private int limit;

    @Override
    public void validatePayload() {
        if (limit < 0)
            limit = 0;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "HomeRequest{" +
                "limit=" + limit +
                '}';
    }
}
