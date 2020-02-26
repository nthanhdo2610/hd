package com.tinhvan.hd.news.service.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.news.config.RabbitConfig;
import com.tinhvan.hd.news.dao.NewsCustomerDao;
import com.tinhvan.hd.news.dao.NewsDao;
import com.tinhvan.hd.news.entity.News;
import com.tinhvan.hd.news.entity.NewsLog;
import com.tinhvan.hd.news.payload.MenuRequest;
import com.tinhvan.hd.news.payload.NewsSearchRequest;
import com.tinhvan.hd.news.payload.NotificationDTO;
import com.tinhvan.hd.news.repository.NewsLogRepository;
import com.tinhvan.hd.news.repository.NewsRepository;
import com.tinhvan.hd.news.service.NewsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;
//    @Autowired
//    private NewsLogDao newsLogDao;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCustomerDao newsCustomerDao;

    @Autowired
    private NewsLogRepository newsLogRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void postNews(News news) {
        newsRepository.save(news);
        NewsLog log = new NewsLog(news);
        newsLogRepository.save(log);
    }

    @Override
    public void updateNews(News news) {
        newsRepository.save(news);
        NewsLog log = new NewsLog(news);
        newsLogRepository.save(log);
        if (news.getAccess() == News.ACCESS.INDIVIDUAL)
            newsCustomerDao.updateByNews(news);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNewsId(news.getId().toString());
        notificationDTO.setTitle(news.getTitle());
        notificationDTO.setContent(news.getNotificationContent());
        if (news.getType() == News.Type.PromotionEvent)
            notificationDTO.setType(HDConstant.NotificationType.EVENT);
        else
            notificationDTO.setType(HDConstant.NotificationType.NEWS);
        notificationDTO.setEndDate(news.getEndDate());
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_UPDATE_NOTIFICATION, notificationDTO);
    }

    @Override
    public void update(News news) {
        newsRepository.save(news);
    }

    @Override
    public News findById(UUID id) {
        return newsDao.findById(id);
    }

    @Override
    public List<News> getListNewsByStatus(Integer status) {
        return newsDao.getListNewsByStatus(status);
    }

    @Override
    public List<News> getListFeatured(int type) {
        return newsDao.getListFeatured(type);
    }

    @Override
    public List<News> findSendNotification() {
        List<News> result = new ArrayList<>();
        List<News> lst = newsDao.findSendNotification();
        if (lst != null) {
            for (News news : lst) {
                if (newsCustomerDao.countListNewsCustomerByNewsId(news.getId()) > 0) {
                    if (news.getIsHandle() == HDConstant.STATUS.ENABLE) {
                        newsCustomerDao.deleteByNewsId(news.getId());
                        result.add(news);
                    }
                    continue;
                }
                if (news.getAccess() == News.ACCESS.GENERAL && news.getStatusNotification() == News.STATUS_NOTIFICATION.NOT_SEND) {
                    continue;
                }
                long now = HDUtil.getUnixTimeNow();
                if (news.getStatus() == HDConstant.STATUS.ENABLE
                        && news.getStartDate() != null
                        && news.getEndDate() != null
                        && HDUtil.getUnixTime(news.getStartDate()) <= now
                        && now <= HDUtil.getUnixTime(news.getEndDate())) {
                    result.add(news);
                }
            }
        }
        return result;
    }

    @Override
    public List<News> find(NewsSearchRequest searchRequest) {
        return newsDao.find(searchRequest);
    }

    @Override
    public int count(NewsSearchRequest searchRequest) {
        return newsDao.count(searchRequest);
    }

    @Override
    public List<News> findIndividual(UUID customerUuid) {
        return newsDao.findIndividual(customerUuid);
    }

    @Override
    public List<News> findGeneral() {
        return newsDao.findGeneral();
    }

    @Override
    public List<News> findHome(int type, int limit) {
        return newsDao.findHome(type, limit);
    }

    @Override
    public List<News> findHomeLogged(UUID customerUuid, int access, int type, int limit) {
        return newsDao.findHomeLogged(customerUuid, access, type, limit);
    }

    @Override
    public List<News> findMenu(MenuRequest menuRequest) {
        return newsDao.findMenu(menuRequest);
    }

    @Override
    public int countMenu(MenuRequest menuRequest) {
        return newsDao.countMenu(menuRequest);
    }

    @Override
    public List<News> findResizeImage() {
        return newsDao.findResizeImage();
    }
}
