package com.tinhvan.hd.repository;

import com.tinhvan.hd.entity.ContractFileTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractFileTemplateRepository extends CrudRepository<ContractFileTemplate, Long> {

    List<ContractFileTemplate> findAllByTypeOrderByIdxAsc(String type);

    ContractFileTemplate findById(Integer id);

    List<ContractFileTemplate> findAllByTypeAndIdxGreaterThanEqual(String type,int idx);

    ContractFileTemplate findByTypeAndIdx(String type,int idx);

    List<ContractFileTemplate> findAllByTypeOrderByIdxDesc(String type);

    List<ContractFileTemplate> findAllByOrderByTypeAscIdxAsc();

    List<ContractFileTemplate> findAllByTypeAndIdxLessThanEqual(String type,int idx);
}
