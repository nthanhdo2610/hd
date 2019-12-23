package com.tinhvan.hd.news.dao;

import com.tinhvan.hd.news.entity.NewsFilterCustomer;

import java.util.List;
import java.util.UUID;

public interface NewsFilterCustomerDAO {
//    void insert(NewsFilterCustomer filterCustomer);
//    void update(NewsFilterCustomer filterCustomer);
//    void delete(int id);
    List<NewsFilterCustomer> findList(UUID newsId);
}
