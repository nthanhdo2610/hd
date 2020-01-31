package com.tinhvan.hd.dao;

import com.tinhvan.hd.dto.SearchStore;
import com.tinhvan.hd.entity.StoreNearYou;

import java.util.List;

public interface StoreNearYouDao {

    List<StoreNearYou> getAllStoreByFilter(SearchStore searchStore, Integer status);
}
