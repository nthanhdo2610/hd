package com.tinhvan.hd.news.repository;

import com.tinhvan.hd.news.entity.NewsCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCustomerRepository extends CrudRepository<NewsCustomer,Long> {
}
