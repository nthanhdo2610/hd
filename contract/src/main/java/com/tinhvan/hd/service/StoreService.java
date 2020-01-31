package com.tinhvan.hd.service;

import com.tinhvan.hd.dto.SearchStore;
import com.tinhvan.hd.entity.Banks;
import com.tinhvan.hd.entity.StoreNearYou;

import java.util.List;

public interface StoreService {

    void saveOrUpdate(StoreNearYou storeNearYou);

    void saveAll(List<StoreNearYou> storeNearYous);

    List<StoreNearYou> getAllStoreByFilter(SearchStore searchStore, Integer status);
}
