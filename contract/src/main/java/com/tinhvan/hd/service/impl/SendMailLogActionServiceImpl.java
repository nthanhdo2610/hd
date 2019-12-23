package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.entity.SendMailLogAction;
import com.tinhvan.hd.repository.SendMailLogActionRepository;
import com.tinhvan.hd.service.SendMailLogActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMailLogActionServiceImpl implements SendMailLogActionService {

    @Autowired
    private SendMailLogActionRepository sendMailLogActionRepository;
    @Override
    public void writeLog(SendMailLogAction sendMailLogAction) {
        sendMailLogActionRepository.save(sendMailLogAction);
    }
}
