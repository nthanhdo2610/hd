package com.tinhvan.hd.news.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.news.dao.NewsLogDao;
import com.tinhvan.hd.news.entity.NewsLog;
import com.tinhvan.hd.news.repository.NewsLogRepository;
import com.tinhvan.hd.news.service.NewsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsLogServiceImpl implements NewsLogService {

    @Autowired
    private NewsLogDao newsLogDao;

    @Autowired
    private NewsLogRepository newsLogRepository;

    @Override
    public void insert(NewsLog log) {
        newsLogRepository.save(log);
    }
}
