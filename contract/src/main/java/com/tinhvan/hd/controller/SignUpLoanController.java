package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.service.HDMiddleService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/signUpLoan")
public class SignUpLoanController extends HDController {

    @Autowired
    private HDMiddleService hdMiddleService;

    /**
     * Create a new SignUpLoan
     *
     * @param req SignUpLoanRequest contain info need to create a new SignUpLoan
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveSignUpLoan(@RequestBody RequestDTO<SignUpLoanRequest> req) {
        
        SignUpLoanRequest signUpLoanRequest = req.init();
        try {
            hdMiddleService.insertSignUpLoan(signUpLoanRequest);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(null);
    }

    /**
     * Find info of SignUpLoan
     *
     * @param req SearchSignUpLoan contain info need filter
     * @return info SignUpLoan
     */
    @PostMapping("/search")
    public ResponseEntity<?> listByIsSent(@RequestBody RequestDTO<SearchSignUpLoan> req) {
        
        SearchSignUpLoan search = req.init();
        List<ResultSearchSignUpLoan> resultSearchSignUpLoans = null;
        HashMap<String, Object> map = new HashMap<>();
        try {

            int total = 0;

            resultSearchSignUpLoans = hdMiddleService.searchSignUpLoan(search);

            if (resultSearchSignUpLoans != null && resultSearchSignUpLoans.size() > 0) {
                total = resultSearchSignUpLoans.get(0).getTotal();
                // sort payment
                Collections.sort(resultSearchSignUpLoans, new Comparator<ResultSearchSignUpLoan>() {
                    public int compare(ResultSearchSignUpLoan p1, ResultSearchSignUpLoan p2) {
                        return Long.valueOf(p2.getCreatedAt().getTime()).compareTo(p1.getCreatedAt().getTime());
                    }
                });
            }

            map.put("data",resultSearchSignUpLoans);
            map.put("total",total);

        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return ok(map);
    }

    /**
     * Update a new SignUpLoan exist
     *
     * @param req UpdateSignUpLoan contain info need to update SignUpLoan
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<UpdateSignUpLoan> req) {
        
        UpdateSignUpLoan updateSignUpLoan = req.init();
        try {
            hdMiddleService.updateSignUpLoan(updateSignUpLoan);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok(null);
    }
}
