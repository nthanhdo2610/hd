package com.tinhvan.hd.repository;

import com.tinhvan.hd.model.ConfigContractTypeBackground;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigContractTypeBackgroundRepository extends CrudRepository<ConfigContractTypeBackground,Integer> {
    List<ConfigContractTypeBackground> findAll();
    Optional<ConfigContractTypeBackground> findByContractType(String contractType);
}
