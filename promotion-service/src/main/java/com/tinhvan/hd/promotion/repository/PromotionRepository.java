package com.tinhvan.hd.promotion.repository;

import com.tinhvan.hd.promotion.entity.Promotion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends CrudRepository<Promotion,Long> {
}
