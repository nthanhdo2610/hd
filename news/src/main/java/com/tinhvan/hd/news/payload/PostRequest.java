package com.tinhvan.hd.news.payload;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;

import java.util.List;

public class PostRequest implements HDPayload {
    private NewsRequest news;
    List<FilterCustomerRequest> filters;

    @Override
    public String toString() {
        return "PostRequest{" +
                "news=" + news +
                ", filters=" + filters +
                '}';
    }

    @Override
    public void validatePayload() {
        if (news == null)
            throw new BadRequestException(1306, "invalid news");
        news.validatePayload();
        if(filters!=null&&filters.size()>0)
            filters.forEach(filterCustomer -> filterCustomer.validatePayload());
    }

    public NewsRequest getNews() {
        return news;
    }

    public void setNews(NewsRequest news) {
        this.news = news;
    }

    public List<FilterCustomerRequest> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterCustomerRequest> filters) {
        this.filters = filters;
    }
}
