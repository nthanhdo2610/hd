package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.ContractFileTemplate;
import com.tinhvan.hd.repository.ContractFileTemplateRepository;
import com.tinhvan.hd.service.ContractFileTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractFileTemplateServiceImpl implements ContractFileTemplateService {
//    @Autowired
//    private ContractFileTemplateDao contractFileTemplateDao;
    @Autowired
    private ContractFileTemplateRepository contractFileTemplateRepository;
    @Override
    public List<ContractFileTemplate> findByType(String type) {
        //return contractFileTemplateDao.findByType(type);
        return contractFileTemplateRepository.findAllByTypeOrderByIdxAsc(type);
    }

    @Override
    public List<ContractFileTemplate> findAll() {
        return contractFileTemplateRepository.findAllByOrderByTypeAscIdxAsc();
    }

    @Override
    public ContractFileTemplate saveOrUpdate(ContractFileTemplate contractFileTemplate) {
        return contractFileTemplateRepository.save(contractFileTemplate);
    }

    @Override
    public void delete(ContractFileTemplate contractFileTemplate) {
        contractFileTemplateRepository.delete(contractFileTemplate);
    }

    @Override
    public ContractFileTemplate findById(Integer id) {
        return contractFileTemplateRepository.findById(id);
    }

    @Override
    public List<ContractFileTemplate> findByTypeAndIdx(String type, int idx) {
        return contractFileTemplateRepository.findAllByTypeAndIdxGreaterThanEqual(type,idx);
    }

    @Override
    public ContractFileTemplate getTemplateFileByTypeAndIdx(String type, int idx) {
        return contractFileTemplateRepository.findByTypeAndIdx(type,idx);
    }

    @Override
    public void saveOrUpdateAll(List<ContractFileTemplate> fileTemplates) {
        contractFileTemplateRepository.saveAll(fileTemplates);
    }

    @Override
    public List<ContractFileTemplate> findByTypeOrderByDesc(String type) {
        return contractFileTemplateRepository.findAllByTypeOrderByIdxDesc(type);
    }

    @Override
    public List<ContractFileTemplate> findAllByTypeAndIdxLessThanEqual(String type, int idx) {
        return contractFileTemplateRepository.findAllByTypeAndIdxLessThanEqual(type,idx);
    }
}
