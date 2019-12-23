package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.ContractFileTemplate;

import java.util.List;

public interface ContractFileTemplateService {

    List<ContractFileTemplate> findByType(String type);

    List<ContractFileTemplate> findAll();

    ContractFileTemplate saveOrUpdate(ContractFileTemplate contractFileTemplate);

    void delete(ContractFileTemplate contractFileTemplate);

    ContractFileTemplate findById(Integer id);

    List<ContractFileTemplate> findByTypeAndIdx(String type,int idx);

    ContractFileTemplate getTemplateFileByTypeAndIdx(String type,int idx);

    void saveOrUpdateAll(List<ContractFileTemplate> fileTemplates);

    List<ContractFileTemplate> findByTypeOrderByDesc(String type);

     List<ContractFileTemplate> findAllByTypeAndIdxLessThanEqual(String type,int idx);
}
