package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.SendMailLogAction;

public interface SendMailLogActionService {
    void writeLog(SendMailLogAction sendMailLogAction);
}
