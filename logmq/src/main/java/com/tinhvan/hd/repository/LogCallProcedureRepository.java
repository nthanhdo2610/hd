package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.LogCallProcedureMiddleDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogCallProcedureRepository extends CrudRepository<LogCallProcedureMiddleDB,Long> {
}
