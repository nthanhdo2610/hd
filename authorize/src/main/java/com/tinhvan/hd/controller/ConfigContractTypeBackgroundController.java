package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.*;
import com.tinhvan.hd.base.enities.CustomerLogAction;
import com.tinhvan.hd.base.enities.StaffLogAction;
import com.tinhvan.hd.base.file.FileS3DTORequest;
import com.tinhvan.hd.base.file.FileS3DTOResponse;
import com.tinhvan.hd.base.file.MimeTypes;
import com.tinhvan.hd.bean.ConfigContractTypeBackgroundInterestRate;
import com.tinhvan.hd.bean.ConfigContractTypeBackgroundKindOffer;
import com.tinhvan.hd.bean.ConfigContractTypeBackgroundUpdate;
import com.tinhvan.hd.bean.ConfigStaffListContractTypeRespon;
import com.tinhvan.hd.file.service.FileStorageService;
import com.tinhvan.hd.model.ConfigContractTypeBackground;
import com.tinhvan.hd.service.ConfigContractTypeBackgroundService;
import com.tinhvan.hd.service.ConfigStaffService;
import com.tinhvan.hd.service.CustomerLogActionService;
import com.tinhvan.hd.service.StaffLogActionService;
import com.tinhvan.hd.utils.WriteLog;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/config_contract_type_background")
public class ConfigContractTypeBackgroundController extends HDController {
    @Autowired
    ConfigContractTypeBackgroundService configContractTypeBackgroundService;

    @Autowired
    FileStorageService fileLocalStorageService;

    @Autowired
    private ConfigStaffService configStaffService;

    @Autowired
    WriteLog log;

    private final String logName = "Loại hợp đồng";

    private Invoker invoker = new Invoker();
    @Value("${app.module.filehandler.service.url}")
    private String urlFileHandlerRequest;

    @Value("${app.module.staff.service.url}")
    private String urlStaffRequest;

    /**
     * Update ConfigContractTypeBackground exist lending app
     *
     * @param req ConfigContractTypeBackgroundUpdate contain information update
     *
     * @return http status code and ConfigContractTypeBackground
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ConfigContractTypeBackgroundUpdate> req) {
        ConfigContractTypeBackgroundUpdate configContractTypeBackgroundUpdate = req.init();
        ConfigContractTypeBackground configContractTypeBackground = configContractTypeBackgroundService.findById(configContractTypeBackgroundUpdate.getId());
        if (configContractTypeBackground != null) {
            ConfigContractTypeBackground valueOld = configContractTypeBackground;
            try {
                configContractTypeBackground.setId(configContractTypeBackgroundUpdate.getId());
                configContractTypeBackground.setBackgroupImageLink(configContractTypeBackgroundUpdate.getBackgroupImageLink());
                configContractTypeBackground.setContractName(configContractTypeBackgroundUpdate.getContractName());
                configContractTypeBackground.setContractType(configContractTypeBackgroundUpdate.getContractType());
                configContractTypeBackground.setModifiedAt(req.now());
                configContractTypeBackground.setModifiedBy(req.jwt().getUuid());
                configContractTypeBackgroundService.insertOrUpdate(configContractTypeBackground);
                //write log
                log.writeLogAction(req, logName, "update", configContractTypeBackgroundUpdate.toString(), valueOld.toString(), configContractTypeBackground.toString(),"", "");
            } catch (Exception ex) {
                throw new BadRequestException(1246, ex.getMessage());
            }
            return ok(configContractTypeBackground);
        }
        throw new BadRequestException(1246, "update ConfigContractTypeBackground error");
    }

    /**
     * Get list ConfigContractTypeBackground exist lending app
     *
     * @param req EmptyPayload
     *
     * @return http status code and list ConfigContractTypeBackground
     */
    @PostMapping("/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigContractTypeBackground> listResult = configContractTypeBackgroundService.list();
        //write log
        log.writeLogAction(req, logName, "list", "", "", listResult.toString(), "", "");

        return ok(listResult);
    }

    /**
     * Update ConfigContractTypeBackground(setBackgroupImageLink)  exist lending app
     *
     * @param req ConfigContractTypeBackgroundUpdate contain information update
     *
     * @return http status code and ConfigContractTypeBackground
     */
    @PostMapping(value = "/image")
    public ResponseEntity<?> image(@RequestBody RequestDTO<ConfigContractTypeBackgroundUpdate> req) {
        ConfigContractTypeBackgroundUpdate configContractTypeBackgroundUpdate = req.init();
        String fileName = configContractTypeBackgroundUpdate.getBackgroupImageLink();
        String contractType = configContractTypeBackgroundUpdate.getContractType();
        ConfigContractTypeBackground configContractTypeBackground = configContractTypeBackgroundService.findById(configContractTypeBackgroundUpdate.getId());
        if (configContractTypeBackground != null) {
            ConfigContractTypeBackground valueOld = configContractTypeBackground;

            configContractTypeBackground.setContractName(configContractTypeBackgroundUpdate.getContractName());
            configContractTypeBackground.setContractType(contractType);
            configContractTypeBackground.setModifiedAt(req.now());
            configContractTypeBackground.setModifiedBy(req.jwt().getUuid());
            if (!fileName.equals("")) {
                String url = invokeFileHandlerS3(req.jwt().getUuid(), fileName, contractType);
                configContractTypeBackground.setBackgroupImageLink(url);
            }
            //write log
            log.writeLogAction(req, logName, "update", configContractTypeBackgroundUpdate.toString(), valueOld.toString(), configContractTypeBackground.toString(),"", "");

            return ok(configContractTypeBackgroundService.insertOrUpdate(configContractTypeBackground));
        }
        throw new BadRequestException(1215, "object is null");
    }

    /**
     * find ConfigContractTypeBackground exist lending app
     *
     * @param req IdPayload contain information find
     *
     * @return http status code and list ConfigContractTypeBackground
     */
    @PostMapping("/find")
    public ResponseEntity<?> findByContractCode(@RequestBody RequestDTO<IdPayload> req) {
        IdPayload idPayload = req.init();
        String contractCode = String.valueOf(idPayload.getId());
        List<ConfigContractTypeBackground> listResult = new ArrayList<>();
        List<String> listContractCode = Arrays.asList(contractCode.split(","));
        List<ConfigStaffListContractTypeRespon> list = configStaffService.getListByContractType();
        if (!ObjectUtils.isEmpty(listContractCode)) {
            for (String s : listContractCode) {
                ConfigContractTypeBackgroundKindOffer contractTypeBackgroundKindOffer = getKindOffer(list, s);
                ConfigContractTypeBackground contractTypeBackground = configContractTypeBackgroundService.findByContractType(contractTypeBackgroundKindOffer.getContractType());
                if (contractTypeBackground != null) {
                    contractTypeBackground.setContractName(contractTypeBackgroundKindOffer.getContractName());
                }else{
                    contractTypeBackground = new ConfigContractTypeBackground();
                }
                listResult.add(contractTypeBackground);
            }
        }
        return ok(listResult);
    }

    /**
     * get interest rate on lending app
     *
     * @param req IdPayload contain information interestRate
     *
     * @return http status code and list interest rate
     */
    @PostMapping("/interest_rate")
    public ResponseEntity<?> interestRate(@RequestBody RequestDTO<EmptyPayload> req) {
        List<ConfigContractTypeBackgroundInterestRate> list = new ArrayList<>();
        list.add(new ConfigContractTypeBackgroundInterestRate("CL",24.26));
        list.add(new ConfigContractTypeBackgroundInterestRate("MC",19.29));
        list.add(new ConfigContractTypeBackgroundInterestRate("ED",12.38));
        list.add(new ConfigContractTypeBackgroundInterestRate("MB",12.38));
        //write log
        log.writeLogAction(req, logName, "list", "", "", list.toString(), "", "");

        return ok(list);
    }

    /**
     * Invoke FileHandlerS3
     *
     * @param employeeUUID
     * @param fileNew
     * @param type
     *
     * @return http status code and url image s3
     */
    private String invokeFileHandlerS3(UUID employeeUUID, String fileNew, String type) {
        String url = "";
        //create object to request upload s3 server
        FileS3DTORequest s3DTO = new FileS3DTORequest();
        List<FileS3DTORequest.FileReq> lst = new ArrayList<>();
        String b64 = fileLocalStorageService.loadFileAsBase64(fileNew);
        lst.add(new FileS3DTORequest.FileReq(employeeUUID.toString(), type,
                "config_contract_type_background",
                MimeTypes.lookupMimeType(FilenameUtils.getExtension(fileNew)),
                b64, "", true));
        s3DTO.setFiles(lst);

        ResponseDTO<FileS3DTOResponse> rs = invoker.call(urlFileHandlerRequest + "/upload", s3DTO, new ParameterizedTypeReference<ResponseDTO<FileS3DTOResponse>>() {
        });

        if (rs.getCode() != 200) {
            throw new BadRequestException(1226, "upload file error");
        }
        url = rs.getPayload().getFiles().get(0).getUri();
        fileLocalStorageService.deleteFile(fileNew);

        return url;
    }

    /**
     * get kind offer on lending app
     *
     * @param list
     * @param contractCode
     *
     * @return http status code and ConfigContractTypeBackgroundKindOffer
     */
    private ConfigContractTypeBackgroundKindOffer getKindOffer(List<ConfigStaffListContractTypeRespon> list, String contractCode) {
        boolean check = false;
        String contractType = "";
        String contractName = "";
        ConfigContractTypeBackgroundKindOffer contractTypeBackgroundKindOffer = new ConfigContractTypeBackgroundKindOffer();
        if (!ObjectUtils.isEmpty(list)) {
            for (ConfigStaffListContractTypeRespon cs : list) {
                String[] arr = cs.getValue().split(";");
                for (int i = 0; i < arr.length; i++) {
                    if (contractCode.startsWith(arr[i])) {
                        switch (cs.getKey()) {
                            case "contract_type_cl_begin_with":
                                contractType = "CL";
                                contractName = "Vay tiền mặt";
                                check = true;
                                break;
                            case "contract_type_clo_begin_with":
                                contractType = "CLO";
                                contractName = "";
                                check = true;
                                break;
                            case "contract_type_ed_begin_with":
                                contractType = "ED";
                                contractName = "Vay mua điện máy";
                                check = true;
                                break;
                            case "contract_type_mc_begin_with":
                                contractType = "MC";
                                contractName = "Vay mua xe máy";
                                check = true;
                                break;
                            default:
                                break;
                        }
                        if (check) {
                            contractTypeBackgroundKindOffer.setContractType(contractType);
                            contractTypeBackgroundKindOffer.setContractName(contractName);
                            return contractTypeBackgroundKindOffer;
                        }
                    }
                }

            }
        }
        return contractTypeBackgroundKindOffer;
    }

}
