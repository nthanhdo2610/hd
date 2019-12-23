package com.tinhvan.hd.utils;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.service.CustomerLogActionService;
import com.tinhvan.hd.service.StaffLogActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WriteLog {

    @Autowired
    private CustomerLogActionService customerLogActionService;

    @Autowired
    private StaffLogActionService staffLogActionService;

    public void writeLogAction(RequestDTO req, String name, String action, String para, String oldValues, String newValues, String contractCode, String type) {
        try {
            UUID uuidObject = null;
            if (req.jwt() != null) {
                uuidObject = req.jwt().getUuid();
            }

            if (req.jwt() != null && req.jwt().getRole() != HDConstant.ROLE.CUSTOMER) {
                StaffLogAction staffLogAction = new StaffLogAction();
                staffLogAction.setObjectName(name);
                staffLogAction.setAction(action);
                staffLogAction.setCreatedAt(req.now());
                staffLogAction.setCreatedBy(uuidObject);
                staffLogAction.setStaffId(uuidObject);
                staffLogAction.setPara(para);
                staffLogAction.setDevice(req.environment());
                staffLogAction.setValueOld(oldValues);
                staffLogAction.setValueNew(newValues);
                staffLogAction.setType(type);
                staffLogActionService.createMQ(staffLogAction);
            } else {
                CustomerLogAction customerLogAction = new CustomerLogAction();
                customerLogAction.setCreatedAt(req.now());
                customerLogAction.setDevice(req.environment());
                customerLogAction.setObjectName(name);
                customerLogAction.setAction(action);
                customerLogAction.setPara(para);
                customerLogAction.setValueOld(oldValues);
                customerLogAction.setValueNew(newValues);
                customerLogAction.setType(type);
                customerLogAction.setCustomerId(uuidObject);
                customerLogAction.setCreatedBy(uuidObject);
                customerLogAction.setContractCode(contractCode);
                customerLogActionService.createMQ(customerLogAction);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}