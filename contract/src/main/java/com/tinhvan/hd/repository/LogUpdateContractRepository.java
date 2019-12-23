package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.LogUpdateContractWhenLogin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogUpdateContractRepository extends CrudRepository<LogUpdateContractWhenLogin,Long> {
}
