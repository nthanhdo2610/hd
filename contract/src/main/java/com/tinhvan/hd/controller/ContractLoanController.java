package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.dto.ContractLoanRequest;
import com.tinhvan.hd.entity.ContractLoan;
import com.tinhvan.hd.service.ContractLoanService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contract/contract_loan")
public class ContractLoanController extends HDController {

    @Autowired
    private ContractLoanService contractLoanService;

    /**
     * Create ContractLoan
     *
     * @param req ContractLoanRequest contain info request
     * @return ContractLoan created successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RequestDTO<ContractLoanRequest> req){
        
        ContractLoanRequest contractLoanRequest = req.init();
        ContractLoan contractLoan = new ContractLoan();
        contractLoan.init(contractLoanRequest);
        contractLoan.setCreatedAt(req.now());
        contractLoanService.create(contractLoan);
        return ok(contractLoan);
    }

    /**
     * Update ContractLoan
     *
     * @param req ContractLoanRequest contain info request
     * @return ContractLoan updated successfully
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<ContractLoanRequest> req){
        
        ContractLoanRequest contractLoanRequest = req.init();
        if(contractLoanRequest.getId()==0)
            return badRequest(1418);
        ContractLoan contractLoan = contractLoanService.getById(contractLoanRequest.getId());
        if(contractLoan==null)
            return badRequest(1418);
        contractLoan.init(contractLoanRequest);
        contractLoan.setModifiedAt(req.now());
        contractLoanService.update(contractLoan);
        return ok(contractLoan);
    }
}
