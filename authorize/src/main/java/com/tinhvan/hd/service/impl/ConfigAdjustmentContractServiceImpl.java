package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.bean.ConfigAdjustmentContractListIsCheck;
import com.tinhvan.hd.model.ConfigAdjustmentContract;
import com.tinhvan.hd.repository.ConfigAdjustmentContractRepository;
import com.tinhvan.hd.service.ConfigAdjustmentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.tinhvan.hd.dao.ConfigAdjustmentContractDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConfigAdjustmentContractServiceImpl implements ConfigAdjustmentContractService {
//    @Autowired
//    ConfigAdjustmentContractDAO configAdjustmentContractDAO;

    @Autowired
    private ConfigAdjustmentContractRepository adjustmentContractRepository;

    @Override
    public void create(ConfigAdjustmentContract object) {
        adjustmentContractRepository.save(object);
    }

    @Override
    public void update(ConfigAdjustmentContract object) {
        adjustmentContractRepository.save(object);
    }

    @Override
    public List<ConfigAdjustmentContract> getList(int status) {
        return adjustmentContractRepository.findAllByStatusOrderByIdx(status);
    }

    @Override
    public List<ConfigAdjustmentContractListIsCheck> getListByIsCheckDocument() {
        return adjustmentContractRepository.getListByIsCheckDocument();
    }

    @Override
    public ConfigAdjustmentContract getConfigAdjustmentContract(String code) {
        return adjustmentContractRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<String> findAllByCodeIn(List<String> codes) {
        List<String> listResult = new ArrayList<>();
        List<ConfigAdjustmentContract> list = adjustmentContractRepository.findAllByCodeInOrderByIdxAsc(codes);
        if(list !=null && !list.isEmpty()){
            for(ConfigAdjustmentContract cf : list){
                listResult.add(cf.getName());
            }
        }
        return listResult;
    }
}
