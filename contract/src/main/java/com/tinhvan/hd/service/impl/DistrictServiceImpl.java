package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.District;
import com.tinhvan.hd.repository.DistrictRepository;
import com.tinhvan.hd.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {


    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistrictByProvinceId(Integer provinceId) {
        return districtRepository.findAllByProvinceId(provinceId);
        /*if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }*/
    }
}
