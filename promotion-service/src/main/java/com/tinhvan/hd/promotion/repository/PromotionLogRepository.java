package com.tinhvan.hd.promotion.repository;

import com.tinhvan.hd.promotion.entity.PromotionLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionLogRepository extends CrudRepository<PromotionLog,Long> {
}
