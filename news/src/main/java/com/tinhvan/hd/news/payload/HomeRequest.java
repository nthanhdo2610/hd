package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.HDPayload;

public class HomeRequest implements HDPayload  {
    private int limit;
    int type;

    @Override
    public void validatePayload() {
        if (limit < 0)
            limit = 0;
        if (type < 0)
            type = 0;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HomeRequest{" +
                "limit=" + limit +
                ", type=" + type +
                '}';
    }
}
