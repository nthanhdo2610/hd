package com.tinhvan.hd.news.service.impl;

import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.news.dao.NewsCustomerDao;
import com.tinhvan.hd.news.entity.NewsCustomer;
import com.tinhvan.hd.news.repository.NewsCustomerRepository;
import com.tinhvan.hd.news.service.NewsCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public class NewsCustomerServiceImpl implements NewsCustomerService {

    @Autowired
    private NewsCustomerDao newsCustomerDao;

    @Autowired
    private NewsCustomerRepository newsCustomerRepository;


    @Override
    public void insert(NewsCustomer newsCustomer) {
        newsCustomerRepository.save(newsCustomer);
    }

    @Override
    public void update(NewsCustomer newsCustomer) {
        newsCustomerRepository.save(newsCustomer);
    }

    @Override
    public void delete(NewsCustomer newsCustomer) {
        newsCustomerRepository.delete(newsCustomer);
    }

    @Override
    public NewsCustomer findById(UUID id) {
        return newsCustomerDao.findById(id);
    }

    @Override
    public List<NewsCustomer> getListNewsCustomerByCustomer(UUID customerUuid) {
        return newsCustomerDao.getListNewsCustomerByCustomer(customerUuid);
    }

    @Override
    public List<NewsCustomer> getListNewsCustomerByNewsId(UUID newsId) {
        return newsCustomerDao.getListNewsCustomerByNewsId(newsId);
    }

    @Override
    public NewsCustomer find(UUID newsId, UUID customerUuid) {
        return newsCustomerDao.find(newsId, customerUuid);
    }

    @Override
    public void saveAll(List<NewsCustomer> list) {
        newsCustomerRepository.saveAll(list);
    }

    @Override
    public List<NewsCustomer> findCustomerAndSendNotification() {
        return newsCustomerDao.findCustomerAndSendNotification();
    }

    @Override
    public boolean validateSendNotification(NewsCustomer newsCustomer) {
        int count = newsCustomerDao.validateSendNotification(newsCustomer);
        if(count==0)
            return true;
        return false;
    }
}
