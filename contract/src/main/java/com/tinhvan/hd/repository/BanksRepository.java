package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.Banks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BanksRepository extends CrudRepository<Banks,Long> {

    List<Banks> findAllByStatus(Integer status);
}
