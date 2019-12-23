package com.tinhvan.hd.repository;

import com.tinhvan.hd.bean.ConfigAdjustmentContractListIsCheck;
import com.tinhvan.hd.model.ConfigAdjustmentContract;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigAdjustmentContractRepository extends CrudRepository<ConfigAdjustmentContract, Integer> {

    List<ConfigAdjustmentContract> findAllByStatusOrderByIdx(int status);

    @Query(value = "SELECT new com.tinhvan.hd.bean.ConfigAdjustmentContractListIsCheck(code, name) FROM ConfigAdjustmentContract WHERE isCheckDocument = 1 order by idx asc ")
    List<ConfigAdjustmentContractListIsCheck> getListByIsCheckDocument();

    Optional<ConfigAdjustmentContract> findByCode(String code);

    List<ConfigAdjustmentContract> findAllByCodeInOrderByIdxAsc(List<String> codes);

}
