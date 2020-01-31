package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.ConfigSendMailDtlDAO;
import com.tinhvan.hd.model.ConfigSendMailDtl;
import com.tinhvan.hd.repository.ConfigSendMailDtlRepository;
import com.tinhvan.hd.service.ConfigSendMailDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigSendMailDtlServiceImpl implements ConfigSendMailDtlService {

    @Autowired
    private ConfigSendMailDtlRepository sendMailDtlRepository;
    @Autowired
    private ConfigSendMailDtlDAO configSendMailDtlDAO;

    @Override
    public ConfigSendMailDtl saveOrUpdate(ConfigSendMailDtl configSendMailDtl) {
        return sendMailDtlRepository.save(configSendMailDtl);
    }

    @Override
    public void saveOrUpdateAll(List<ConfigSendMailDtl> configSendMailDtls) {
        sendMailDtlRepository.saveAll(configSendMailDtls);
    }

    @Override
    public ConfigSendMailDtl getListByConfigSendMailIdAndProAndDis(Long configSendMaiId, String province, String district) {
        return sendMailDtlRepository.findAllByConfigSendMailIdAndProvinceAndDistrict(configSendMaiId, province, district);
    }

    @Override
    public List<ConfigSendMailDtl> findByConfigSendMailId(long configId) {
        return configSendMailDtlDAO.findByConfigSendMailId(configId);
    }

    @Override
    public List<ConfigSendMailDtl> generateTemplate() {
        return configSendMailDtlDAO.generateTemplate();
    }
}
