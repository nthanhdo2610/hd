package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.District;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DistrictRepository extends CrudRepository<District,Long> {

    List<District> findAllByProvinceId(Integer provinceId);


}
