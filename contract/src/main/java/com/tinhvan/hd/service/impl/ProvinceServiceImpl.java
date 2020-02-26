package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.Province;
import com.tinhvan.hd.repository.ProvinceRepository;
import com.tinhvan.hd.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> getAllProvince() {
        return provinceRepository.findAll(Sort.by(Sort.Direction.ASC, "idx", "provinceName"));
    }
}
