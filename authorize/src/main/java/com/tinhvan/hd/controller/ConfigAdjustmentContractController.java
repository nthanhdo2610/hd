package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.bean.ConfigAdjustmentContractListIsCheck;
import com.tinhvan.hd.bean.ConfigAdjustmentContractUpdate;
import com.tinhvan.hd.model.ConfigAdjustmentContract;
import com.tinhvan.hd.service.ConfigAdjustmentContractService;
import com.tinhvan.hd.service.CustomerLogActionService;
import com.tinhvan.hd.service.StaffLogActionService;
import com.tinhvan.hd.utils.WriteLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/config_adjustment_contract")
public class ConfigAdjustmentContractController extends HDController {
    @Autowired
    ConfigAdjustmentContractService configAdjustmentContractService;

    @Autowired
    WriteLog log;

    private final String logName = "Kiểm tra hồ sơ";

    /**
     * Update ConfigAdjustmentContract exist lending app
     *
     * @param req ConfigAdjustmentContractUpdate contain information update
     *
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ConfigAdjustmentContractUpdate> req) {

        ConfigAdjustmentContractUpdate configAdjustmentContractUpdate = req.init();
        List<ConfigAdjustmentContract> list = configAdjustmentContractUpdate.getList();
        List<ConfigAdjustmentContract> listValuesOld = configAdjustmentContractService.getList(1);
        if (list != null && !list.isEmpty()) {
            for (ConfigAdjustmentContract configAdjustmentContract : list) {
                configAdjustmentContract.setModifiedAt(req.now());
                configAdjustmentContract.setModifiedBy(req.jwt().getUuid());
                configAdjustmentContractService.update(configAdjustmentContract);
            }
            //write log
            log.writeLogAction(req, logName, "update", configAdjustmentContractUpdate.toString(), listValuesOld.toString(), list.toString(), "", "");
            return ok("1");
        }
        return ok("0");
    }

    /**
     * Get list ConfigAdjustmentContract exist lending app
     *
     * @param req EmptyPayload
     *
     * @return http status code and list ConfigAdjustmentContract
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigAdjustmentContract> listResult = configAdjustmentContractService.getList(1);
        //write log
        log.writeLogAction(req, logName, "list", "", "", listResult.toString(), "", "");
        return ok(listResult);
    }

    /**
     * Get list ConfigAdjustmentContractListIsCheck exist lending app
     *
     * @param req EmptyPayload
     *
     * @return http status code and list ConfigAdjustmentContractListIsCheck
     */
    @PostMapping("/list_is_check_document")
    public ResponseEntity<?> getListByIsCheckDocument(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigAdjustmentContractListIsCheck> listResult = configAdjustmentContractService.getListByIsCheckDocument();
        //write log
        log.writeLogAction(req, logName, "list_is_check_document", "", "", listResult.toString(), "", "invoke");

        return ok(listResult);
    }

    /**
     * Get ConfigAdjustmentContract exist lending app
     *
     * @param req object IdPayload contain information getConfigAdjustmentContract
     *
     * @return http status code and object ConfigAdjustmentContract
     */
    @PostMapping("/get_config_adjustment")
    public ResponseEntity<?> getConfigAdjustmentContract(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        String code = (String) idPayload.getId();
        ConfigAdjustmentContract configAdjustmentContract = configAdjustmentContractService.getConfigAdjustmentContract(code);
        //write log
        log.writeLogAction(req, logName, "get_config_adjustment", idPayload.getId().toString(), "", configAdjustmentContract.toString(), "", "invoke");
        return ok(configAdjustmentContract);
    }

    /**
     * Get list name exist lending app
     *
     * @param req object IdPayload contain information findAllByCodeIn
     *
     * @return http status code and list name
     */
    @PostMapping("/get_list_name")
    public ResponseEntity<?> findAllByCodeIn(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        String code = (String) idPayload.getId();
        List<String> codes = Arrays.asList(code.split(","));
        List<String> list = configAdjustmentContractService.findAllByCodeIn(codes);

        //write log
        log.writeLogAction(req, logName, "get_list_name", idPayload.getId().toString(), "", list.toString(), "", "invoke");
        return ok(list);
    }
}
