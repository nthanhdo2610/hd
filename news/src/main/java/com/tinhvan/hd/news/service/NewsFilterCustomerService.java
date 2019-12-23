package com.tinhvan.hd.news.service;

import com.tinhvan.hd.news.entity.NewsFilterCustomer;

import java.util.List;
import java.util.UUID;

public interface NewsFilterCustomerService {
    void insert(NewsFilterCustomer filterCustomer);
    void update(NewsFilterCustomer filterCustomer);
    void delete(int id);
    List<NewsFilterCustomer> findList(UUID newsId);
}
