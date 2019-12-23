package com.tinhvan.hd.repository;

import com.tinhvan.hd.model.Services;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends CrudRepository<Services,Integer> {
}
