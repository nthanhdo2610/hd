package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.LogMail;
import org.springframework.stereotype.Service;

@Service
public interface LogMailService {
    void create(LogMail logMail);
}
