package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.BankDTO;
import com.tinhvan.hd.dto.BankRequest;
import com.tinhvan.hd.dto.ProductionRequest;
import com.tinhvan.hd.entity.Banks;
import com.tinhvan.hd.entity.Production;
import com.tinhvan.hd.service.BankService;
import com.tinhvan.hd.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bank")
public class BankController extends HDController {

    @Autowired
    private BankService bankService;

    /**
     * Get list bank
     *
     * @param req
     * @return list info of bank
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllBank(@RequestBody RequestDTO<EmptyPayload> req) {

        List<BankDTO> list = new ArrayList<>();
        List<Banks> banks = bankService.getAllBankByStatus(1);

        if (banks != null && banks.size() > 0) {
            for (Banks b : banks) {
                BankDTO dto = new BankDTO();
                dto.setCode(b.getCode());
                dto.setName(b.getName());
                list.add(dto);
            }
        }

        return ok(list);
    }

    /**
     * Save bank
     *
     * @param req BankRequest contain info of bank
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveBanks(@RequestBody RequestDTO<BankRequest> req) {

        BankRequest request = req.init();

        List<BankDTO> bankDTOS = request.getData();
        List<Banks> list = new ArrayList<>();
        if (bankDTOS != null && bankDTOS.size() > 0) {
            for (BankDTO dto : bankDTOS) {
                Banks bank = new Banks();
                bank.setCode(dto.getCode());
                bank.setName(dto.getName());
                bank.setStatus(1);
                bank.setCreatedAt(new Date());
                list.add(bank);
            }
        }

        if (list != null && list.size() > 0) {
            bankService.saveAll(list);
        }
        return ok(null);
    }
}
