package com.tinhvan.hd.news.service.impl;

import com.tinhvan.hd.news.dao.NewsFilterCustomerDAO;
import com.tinhvan.hd.news.entity.NewsFilterCustomer;
import com.tinhvan.hd.news.repository.NewsFilterCustomerRepository;
import com.tinhvan.hd.news.service.NewsFilterCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class NewsFilterCustomerServiceImpl implements NewsFilterCustomerService {

    @Autowired
    private NewsFilterCustomerDAO newsFilterCustomerDAO;

    @Autowired
    private NewsFilterCustomerRepository newsFilterCustomerRepository;

    @Override
    public void insert(NewsFilterCustomer filterCustomer) {
        newsFilterCustomerRepository.save(filterCustomer);
    }

    @Override
    public void update(NewsFilterCustomer filterCustomer) {
        newsFilterCustomerRepository.save(filterCustomer);
    }

    @Override
    public void delete(int id) {
        newsFilterCustomerRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public List<NewsFilterCustomer> findList(UUID newsId) {
        return newsFilterCustomerDAO.findList(newsId);
    }
}
