package com.tinhvan.hd.news.service;

import com.tinhvan.hd.news.entity.News;
import com.tinhvan.hd.news.payload.MenuRequest;
import com.tinhvan.hd.news.payload.NewsSearchRequest;

import java.util.List;
import java.util.UUID;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
public interface NewsService {

    void postNews(News news);
    void updateNews(News news);
    News findById(UUID id);
    List<News> getListNewsByStatus(Integer status);
    List<News> getListFeatured(int type);
    List<News> findSendNotification();
    List<News> find(NewsSearchRequest searchRequest);
    int count(NewsSearchRequest searchRequest);
    List<News> findIndividual(UUID customerUuid);
    List<News> findGeneral();
    List<News> findHome(int type, int limit);
    List<News> findHomeLogged(UUID customerUuid, int access, int type, int limit);
    List<News> findMenu(MenuRequest menuRequest);
    int countMenu(MenuRequest menuRequest);
}
