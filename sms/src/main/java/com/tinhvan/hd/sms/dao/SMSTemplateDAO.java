/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinhvan.hd.sms.dao;

import com.tinhvan.hd.sms.bean.SMSTemplateList;
import com.tinhvan.hd.sms.bean.SMSTemplateListRespon;
//import com.tinhvan.hd.sms.filter.SMSTemplateFilter;
import com.tinhvan.hd.sms.model.SMSTemplate;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author LUUBI
 */
public interface SMSTemplateDAO {
//    void create(SMSTemplate object);
//    void update(SMSTemplate object);
    SMSTemplate findByTypeAndLangCode(String smsType, String langCode);
//    List<SMSTemplate> getList(SMSTemplateFilter filter);
//    int num(SMSTemplateFilter filter);
    SMSTemplateListRespon getList(SMSTemplateList smsTemplateList);
    SMSTemplate findByUUID(UUID uuid);
}
