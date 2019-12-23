package com.tinhvan.hd.repository;

import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.model.ConfigStaff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigStaffRepository extends CrudRepository<ConfigStaff,Integer> {
    Optional<ConfigStaff> findByKey(String key);

    @Query("select new com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon(cs.key, cs.value) from ConfigStaff cs where key like 'contract_type%'")
    List<ConfigStaffListContractTypeRespon> getListByContractType();
}
