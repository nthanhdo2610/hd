package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.IdPayload;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.entity.ContractAdjustmentInfo;
import com.tinhvan.hd.service.ContractAdjustmentInfoService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/contract/adjustment_info")
public class ContractAdjustmentInfoController extends HDController {

    @Autowired
    private ContractAdjustmentInfoService contractAdjustmentInfoService;

    /**
     * Create a ContractAdjustmentInfo
     *
     * @param req ContractAdjustmentInfo contain info request
     * @return http status code
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RequestDTO<ContractAdjustmentInfo> req) {

        ContractAdjustmentInfo contractAdjustmentInfo = req.getPayload();
        contractAdjustmentInfo.setCreatedAt(new Date());
        contractAdjustmentInfo.setIsConfirm(0);
        contractAdjustmentInfoService.create(contractAdjustmentInfo);
        return ok(null);
    }

    /**
     * Update a ContractAdjustmentInfo exist
     *
     * @param req ContractAdjustmentInfo contain info request
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ContractAdjustmentInfo> req) {
        return ok(null);
    }

    /**
     * Delete one ContractAdjustmentInfo
     *
     * @param req IdPayload contain id of ContractAdjustmentInfo
     * @return http status code
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody RequestDTO<IdPayload> req) {
        return ok(null);
    }
}
