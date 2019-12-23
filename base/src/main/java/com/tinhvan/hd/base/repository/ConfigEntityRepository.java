package com.tinhvan.hd.base.repository;

import com.tinhvan.hd.base.enities.ConfigEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigEntityRepository extends CrudRepository<ConfigEntity, String> {
    List<ConfigEntity> findAll();
}
