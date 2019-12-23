package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.Banks;
import com.tinhvan.hd.entity.StoreNearYou;
import com.tinhvan.hd.repository.StoreRepository;
import com.tinhvan.hd.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;


    @Override
    public void saveOrUpdate(StoreNearYou storeNearYou) {
        storeRepository.save(storeNearYou);
    }

    @Override
    public void saveAll(List<StoreNearYou> storeNearYous) {
        storeRepository.saveAll(storeNearYous);
    }

    @Override
    public List<StoreNearYou> getAllStore(Integer status) {
        return (List<StoreNearYou>) storeRepository.findAll();
    }
}
