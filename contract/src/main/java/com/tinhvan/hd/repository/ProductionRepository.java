package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.Production;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionRepository extends CrudRepository<Production,Long> {

    List<Production> findAllByType(String type);


}
