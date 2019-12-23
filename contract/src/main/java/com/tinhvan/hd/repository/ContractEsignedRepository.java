package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractEsigned;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ContractEsignedRepository extends CrudRepository<ContractEsigned,Long> {

    ContractEsigned findByContractUuidAndIsSignedAdjustment(UUID contractUuid,Integer isSignedAdjustment);

    List<ContractEsigned> findAllByCustomerUuidAndContractUuid(UUID customerUuid,UUID contractUuid);
}
