package com.tinhvan.hd.dao;

import com.tinhvan.hd.entity.Scheme;

import java.util.List;

public interface SchemeDao {

//    void insertScheme(Scheme role);
//
//    void updateScheme(Scheme role);
//
//    void deleteScheme(Scheme role);
//
//    Scheme getById(Long id);

    Scheme findByScheme(String role);

    List<Scheme> getAll();
    List<Scheme> findBySchemeCode(String schemeCode);
}
