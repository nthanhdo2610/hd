package com.tinhvan.hd.news.repository;

import com.tinhvan.hd.news.entity.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends CrudRepository<News,Long> {
}
