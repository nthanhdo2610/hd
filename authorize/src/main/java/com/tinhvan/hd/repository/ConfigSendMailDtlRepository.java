package com.tinhvan.hd.repository;

import com.tinhvan.hd.model.ConfigSendMailDtl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigSendMailDtlRepository extends CrudRepository<ConfigSendMailDtl, Long> {

    ConfigSendMailDtl findAllByConfigSendMailIdAndProvinceAndDistrict(Long configSendMailId, String province, String district);
}
