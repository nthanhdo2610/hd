package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.District;
import com.tinhvan.hd.entity.Province;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DistrictService {

    List<District> getAllDistrictByProvinceId(Integer provinceId);
}
