package com.tinhvan.hd.service;

import com.tinhvan.hd.model.ConfigSendMailDtl;

import java.util.List;

public interface ConfigSendMailDtlService {

    ConfigSendMailDtl saveOrUpdate(ConfigSendMailDtl configSendMailDtl);

    void saveOrUpdateAll(List<ConfigSendMailDtl> configSendMailDtls);

    ConfigSendMailDtl getListByConfigSendMailIdAndProAndDis(Long configSendMaiId, String province, String district);

    List<ConfigSendMailDtl> findByConfigSendMailId(long configId);
    List<ConfigSendMailDtl> generateTemplate();
}
