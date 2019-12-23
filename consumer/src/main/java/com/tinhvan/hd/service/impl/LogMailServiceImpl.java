package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.LogMail;
import com.tinhvan.hd.repository.LogMailRepository;
import com.tinhvan.hd.service.LogMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogMailServiceImpl implements LogMailService {
    @Autowired
    private LogMailRepository logMailRepository;

    @Override
    public void create(LogMail logMail) {
        logMailRepository.save(logMail);
    }
}
