package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.StoreNearYou;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends CrudRepository<StoreNearYou,Long> {
}
