package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.bean.*;
import com.tinhvan.hd.model.ConfigStaff;
import com.tinhvan.hd.service.ConfigStaffService;
import com.tinhvan.hd.service.CustomerLogActionService;
import com.tinhvan.hd.service.StaffLogActionService;
import com.tinhvan.hd.utils.WriteLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config_staff")
public class ConfigStaffController extends HDController {

    private final String logName = "Cấu hình chung";
    @Autowired
    WriteLog log;
    @Autowired
    private ConfigStaffService configStaffService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CustomerLogActionService customerLogActionService;
    @Autowired
    private StaffLogActionService staffLogActionService;
    private Invoker invoker = new Invoker();
    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    /**
     * Update list ConfigStaff exist lending app
     *
     * @param req ConfigStaffUpdate contain information update
     *
     * @return http status code and list ConfigStaff
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ConfigStaffUpdate> req) {
        ConfigStaffUpdate configStaffUpdate = req.init();
        List<ConfigStaff> oldValues = configStaffService.list();
        List<ConfigStaff> list = configStaffUpdate.getList();
        UUID staffUUID = null;
        if (req.jwt() != null) {
            staffUUID = req.jwt().getUuid();
        }
        List<ConfigStaff> listResult = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (ConfigStaff configStaff : list) {
                configStaff.setModifiedAt(req.now());
                configStaff.setModifiedBy(staffUUID);
                configStaffService.insertOrUpdate(configStaff);
                listResult.add(configStaff);
            }
            //write log
            log.writeLogAction(req, logName, "update", configStaffUpdate.toString(), oldValues.toString(), listResult.toString(), "", "");
        }
        cacheService.forgetAboutThis(HDConstant.SYSTEM_CONFIG);

        return ok(listResult);
    }

    /**
     * get list ConfigStaff exist lending app
     *
     * @param req EmptyPayload
     *
     * @return http status code and list ConfigStaff
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigStaff> listResult = configStaffService.list();
        //write log
        log.writeLogAction(req, logName, "list", "", "", listResult.toString(), "", "");

        return ok(listResult);
    }

    /**
     * get value by key on ConfigStaff exist lending app
     *
     * @param req ConfigStaffGetValue content information getValueByKey
     *
     * @return http status code and value
     */
    @PostMapping("/get_value")
    public ResponseEntity<?> getValueByKey(@RequestBody RequestDTO<ConfigStaffGetValue> req) {
        ConfigStaffGetValue configStaffGetValue = req.init();
        String result = configStaffService.getValueByKey(configStaffGetValue.getKey());
        //write log
        log.writeLogAction(req, logName, "get_value", configStaffGetValue.toString(), "", result, "", "invoke");
        return ok(result);
    }

    /**
     * get kind offer
     *
     * @param req IdPayload content information getKindOfferByContract
     *
     * @return http status code and kind offer
     */
    @PostMapping("/get_kind_offer")
    public ResponseEntity<?> getKindOfferByContract(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        String contractCode = (String) idPayload.getId();
        List<ConfigStaffListContractTypeRespon> list = configStaffService.getListByContractType();
        String result = getKindOffer(list, contractCode);
        //write log
        log.writeLogAction(req, logName, "get_kind_offer", idPayload.getId().toString(), "", result, "", "invoke");
        return ok(result);
    }

    /**
     * check time register eSign
     *
     * @param req EmptyPayload
     *
     * @return http status code and object ConfigStaffCheckTimeRegisterESignResponse
     */
    @PostMapping("/check_time_register_esign")
    public ResponseEntity<?> checkTime(@RequestBody RequestDTO<EmptyPayload> req) {
        ConfigStaffCheckTimeRegisterEsignRespone result = convertValue(req.now());
        //write log
        log.writeLogAction(req, logName, "check_time_register_esign", "", "", result.toString(), "", "");
        return ok(result);
    }

    /**
     * get kind offer on lending app
     *
     * @param list
     * @param contractCode
     *
     * @return kind offer
     */
    private String getKindOffer(List<ConfigStaffListContractTypeRespon> list, String contractCode) {
        boolean check = false;
        String result = "ED";
        if (list != null && !list.isEmpty()) {
            for (ConfigStaffListContractTypeRespon cs : list) {
                String[] arr = cs.getValue().split(";");
                for (int i = 0; i < arr.length; i++) {
                    if (contractCode.startsWith(arr[i])) {
                        switch (cs.getKey()) {
                            case "contract_type_clo_begin_with":
                                result = "CLO";
                                check = true;
                                break;
                            case "contract_type_cl_begin_with":
                                result = "CL";
                                check = true;
                                break;
                            case "contract_type_ed_begin_with":
                                result = "ED";
                                check = true;
                                break;
                            case "contract_type_mc_begin_with":
                                result = "MC";
                                check = true;
                                break;
                            default:
                                break;
                        }
                        if (check)
                            return result;
                    }
                }

            }
        }
        return result;
    }

    /**
     * convert date values eSign_from and eSign_to
     *
     * @param dateRequest
     *
     * @return object ConfigStaffCheckTimeRegisterEsignRespone
     */
    private ConfigStaffCheckTimeRegisterEsignRespone convertValue(Date dateRequest) {
        String esignFrom = configStaffService.getValueByKey("esign_from");
        String[] arrEsignFrom = esignFrom.split(":");
        ConvertValue convertValueEsignFrom = null, convertValueEsignTo = null;
        if (arrEsignFrom.length == 2)
            convertValueEsignFrom = new ConvertValue(Integer.parseInt(arrEsignFrom[0]), Integer.parseInt(arrEsignFrom[1]));
        String esignTo = configStaffService.getValueByKey("esign_to");
        String[] arrEsignTo = esignTo.split(":");
        if (arrEsignTo.length == 2)
            convertValueEsignTo = new ConvertValue(Integer.parseInt(arrEsignTo[0]), Integer.parseInt(arrEsignTo[1]));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dateRequest.getDate());
        if(convertValueEsignFrom != null){
        c.set(Calendar.HOUR_OF_DAY, convertValueEsignFrom.getHour());
        c.set(Calendar.MINUTE, convertValueEsignFrom.getMinute());
        }
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dateFrom = c.getTime();
        if(convertValueEsignTo != null){
        c.set(Calendar.HOUR_OF_DAY, convertValueEsignTo.getHour());
        c.set(Calendar.MINUTE, convertValueEsignTo.getMinute());
        }
        Date dateTo = c.getTime();
        //dateFrom > dateTo
        if(dateFrom.after(dateTo)){
            c.set(Calendar.DAY_OF_MONTH, dateRequest.getDate() + 1);
            dateTo = c.getTime();
        }
        ConfigStaffCheckTimeRegisterEsignRespone checkTimeRegisterEsign;
        if (dateRequest.before(dateTo) && dateRequest.after(dateFrom)) {
            checkTimeRegisterEsign = new ConfigStaffCheckTimeRegisterEsignRespone(1, esignFrom, esignTo);
        } else {
            checkTimeRegisterEsign = new ConfigStaffCheckTimeRegisterEsignRespone(0, esignFrom, esignTo);
        }
        return checkTimeRegisterEsign;
    }

}
