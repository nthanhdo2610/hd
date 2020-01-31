package com.tinhvan.hd.news.dao;

import com.tinhvan.hd.news.entity.News;
import com.tinhvan.hd.news.entity.NewsCustomer;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
public interface NewsCustomerDao {

//    void insert(NewsCustomer newsCustomer);
//
//    void update(NewsCustomer newsCustomer);
//
//    void delete(NewsCustomer newsCustomer);

    NewsCustomer findById(UUID id);

    List<NewsCustomer> getListNewsCustomerByCustomer(UUID customerUuid);

    List<NewsCustomer> getListNewsCustomerByNewsId(UUID newsId);

    int countListNewsCustomerByNewsId(UUID newsId);

    NewsCustomer find(UUID newsId, UUID customerUuid);

    List<NewsCustomer> findCustomerAndSendNotification();

    int validateSendNotification(NewsCustomer newsCustomer);

    void updateByNews(News news);
}
