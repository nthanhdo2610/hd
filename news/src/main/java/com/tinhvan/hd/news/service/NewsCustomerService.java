package com.tinhvan.hd.news.service;

import com.tinhvan.hd.news.entity.NewsCustomer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public interface NewsCustomerService {

    void insert(NewsCustomer newsCustomer);

    void update(NewsCustomer newsCustomer);

    void delete(NewsCustomer newsCustomer);

    NewsCustomer findById(UUID id);

    List<NewsCustomer> getListNewsCustomerByCustomer(UUID customerUuid);

    List<NewsCustomer> getListNewsCustomerByNewsId(UUID newsId);

    NewsCustomer find(UUID newsId, UUID customerUuid);

    void saveAll(List<NewsCustomer> list);

    List<NewsCustomer> findCustomerAndSendNotification();

    boolean validateSendNotification(NewsCustomer newsCustomer);
}
