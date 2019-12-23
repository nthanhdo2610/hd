package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dao.ConfigStaffDao;
import com.tinhvan.hd.model.ConfigStaff;
import com.tinhvan.hd.repository.ConfigStaffRepository;
import com.tinhvan.hd.service.ConfigStaffService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigStaffServiceImpl implements ConfigStaffService {
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    ConfigStaffDao configStaffDao;
    @Autowired
    private ConfigStaffRepository configStaffRepository;

    @Autowired
    public ConfigStaffServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void insertOrUpdate(ConfigStaff config) {
        configStaffRepository.save(config);
        configStaffDao.insertOrUpdate(config);
    }

    @Override
    public List<ConfigStaff> list() {
        return (List<ConfigStaff>) configStaffRepository.findAll();
    }

    @Override
    public String getValueByKey(String key) {
//        return configStaffDao.getValueByKey(key);
        ConfigStaff configStaff = configStaffRepository.findByKey(key).orElse(null);
        if (configStaff != null)
            return configStaff.getValue();
        return "";
    }

    @Override
    public List<ConfigStaffListContractTypeRespon> getListByContractType() {
//        return configStaffDao.getListByContractType();
        return configStaffRepository.getListByContractType();
    }

    @Override
    public ConfigStaff findById(int id) {
        return configStaffRepository.findById(id).orElse(null);
    }

    @Override
    public void createMQ(StaffLogAction object) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_LOG_STAFF_LOG_ACTION, object);
    }
}
