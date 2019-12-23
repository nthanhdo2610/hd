package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.StaffLogAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffLogActionRepository extends CrudRepository<StaffLogAction,Long> {
}
