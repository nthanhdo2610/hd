package com.tinhvan.hd.dao;

import com.tinhvan.hd.model.ConfigSendMailDtl;

import java.util.List;

public interface ConfigSendMailDtlDAO {
    List<ConfigSendMailDtl> findByConfigSendMailId(long configId);
    List<ConfigSendMailDtl> generateTemplate();
}
