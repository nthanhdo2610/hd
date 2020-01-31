package com.tinhvan.hd.service;

import com.tinhvan.hd.model.ConfigSendMail;

import java.util.List;

public interface ConfigSendMailService {

    void saveOrUpdate(ConfigSendMail configSendMail);

    void saveOrUpdateAll(List<ConfigSendMail> configSendMails);

    ConfigSendMail getByTypeAndContractType(Integer type,String typeContract);
}
