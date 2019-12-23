package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.Scheme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeRepository extends CrudRepository<Scheme,Long> {
}
