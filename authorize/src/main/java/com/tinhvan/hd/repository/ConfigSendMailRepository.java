package com.tinhvan.hd.repository;

import com.tinhvan.hd.model.ConfigSendMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigSendMailRepository extends CrudRepository<ConfigSendMail,Long> {

    ConfigSendMail findByTypeAndContractType(Integer type,String contractType);
}
