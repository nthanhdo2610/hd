package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Scheme;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SchemeService {

    void insertScheme(Scheme scheme);

    void updateScheme(Scheme scheme);

    void deleteScheme(Scheme scheme);

    Scheme getById(Long id);

    List<Scheme> getAll();
    List<Scheme> findBySchemeCode(String schemeCode);

}
