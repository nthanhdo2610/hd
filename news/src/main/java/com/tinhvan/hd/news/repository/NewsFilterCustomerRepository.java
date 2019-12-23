package com.tinhvan.hd.news.repository;

import com.tinhvan.hd.news.entity.NewsFilterCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFilterCustomerRepository extends CrudRepository<NewsFilterCustomer,Long> {
}
