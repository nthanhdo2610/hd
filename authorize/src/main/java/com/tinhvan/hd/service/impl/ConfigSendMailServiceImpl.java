package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.model.ConfigSendMail;
import com.tinhvan.hd.repository.ConfigSendMailRepository;
import com.tinhvan.hd.service.ConfigSendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigSendMailServiceImpl implements ConfigSendMailService {

    @Autowired
    private ConfigSendMailRepository configSendMailRepository;

    @Override
    public void saveOrUpdate(ConfigSendMail configSendMail) {
        configSendMailRepository.save(configSendMail);
    }

    @Override
    public void saveOrUpdateAll(List<ConfigSendMail> configSendMails) {
        configSendMailRepository.saveAll(configSendMails);
    }

    @Override
    public ConfigSendMail getByTypeAndContractType(Integer type, String contractType) {
        return configSendMailRepository.findByTypeAndContractType(type,contractType);
    }
}
