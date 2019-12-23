package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.Province;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends CrudRepository<Province,Long> {


}
