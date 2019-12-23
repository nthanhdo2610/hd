package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.dto.DisbursementInfo;
import com.tinhvan.hd.dto.ResultDisbursementInfo;
import com.tinhvan.hd.dto.SearchDisbursementInfo;
import com.tinhvan.hd.dto.UpdateDisbursementInfo;
import com.tinhvan.hd.service.HDMiddleService;
import com.tinhvan.hd.service.CustomerLogActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/v1/contract/disbursementInfo")
public class ContractDisbursementInfoController extends HDController {

    @Autowired
    private HDMiddleService hdMiddleService;

    @Autowired
    private CustomerLogActionService customerLogActionService;

    /**
     * Create a DisbursementInfo
     *
     * @param req DisbursementInfo contain info request
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveSignUpLoan(@RequestBody RequestDTO<DisbursementInfo> req) {

        DisbursementInfo disbursementInfo = req.init();
        try {
            hdMiddleService.insertDisbursementInfo(disbursementInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return ok(null);
    }

    /**
     * Find list DisbursementInfo
     *
     * @param req SearchDisbursementInfo contain info filter request
     * @return list ResultDisbursementInfo
     */
    @PostMapping("/search")
    public ResponseEntity<?> listByIsSent(@RequestBody RequestDTO<SearchDisbursementInfo> req) {

        SearchDisbursementInfo search = req.init();
        List<ResultDisbursementInfo> resultDisbursementInfos = null;
        try {
            resultDisbursementInfos = hdMiddleService.getDisbursementInfoByIsSent(search);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return ok(resultDisbursementInfos);
    }

    /**
     * Update DisbursementInfo
     *
     * @param req UpdateDisbursementInfo contain info need update
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<UpdateDisbursementInfo> req) {

        UpdateDisbursementInfo updateDisbursementInfo = req.init();
        try {
            hdMiddleService.updateDisbursementInfo(updateDisbursementInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(null);
    }

}
