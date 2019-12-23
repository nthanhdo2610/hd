package com.tinhvan.hd.news.repository;

import com.tinhvan.hd.news.entity.NewsLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLogRepository extends CrudRepository<NewsLog,Long> {
}
