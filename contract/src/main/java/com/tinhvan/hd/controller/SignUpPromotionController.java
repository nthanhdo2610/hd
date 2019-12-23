package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.dto.ResultSearchSignUpPromotion;
import com.tinhvan.hd.dto.SearchSignUpPromotion;
import com.tinhvan.hd.dto.SignUpPromotionRequest;
import com.tinhvan.hd.dto.UpdateSignUpPromotion;
import com.tinhvan.hd.service.HDMiddleService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/signUpPromotion")
public class SignUpPromotionController extends HDController {

    @Autowired
    private HDMiddleService hdMiddleService;

    /**
     * Create a new SignUpPromotion
     *
     * @param req SignUpPromotionRequest contain info need to create a new SignUpPromotion
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveSignUpPromotion(@RequestBody RequestDTO<SignUpPromotionRequest> req) {

        SignUpPromotionRequest signUpPromotionRequest = req.init();
        try {
            hdMiddleService.insertSignUpPromotion(signUpPromotionRequest);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok();
    }

    /**
     * Find info of SignUpPromotion
     *
     * @param req SearchSignUpPromotion contain info need filter
     * @return info SignUpPromotion
     */
    @PostMapping("/search")
    public ResponseEntity<?> listByIsSent(@RequestBody RequestDTO<SearchSignUpPromotion> req) {

        SearchSignUpPromotion search = req.init();
        List<ResultSearchSignUpPromotion> resultSearchSignUpPromotionList = null;
        HashMap<String, Object> map = new HashMap<>();
        try {

            int total = 0;

            resultSearchSignUpPromotionList = hdMiddleService.searchSignUpPromotion(search);

            if (resultSearchSignUpPromotionList != null && resultSearchSignUpPromotionList.size() > 0) {
                total = resultSearchSignUpPromotionList.get(0).getTotal();
                // sort payment
                Collections.sort(resultSearchSignUpPromotionList, new Comparator<ResultSearchSignUpPromotion>() {
                    public int compare(ResultSearchSignUpPromotion p1, ResultSearchSignUpPromotion p2) {
                        return Long.valueOf(p2.getCreatedAt().getTime()).compareTo(p1.getCreatedAt().getTime());
                    }
                });
            }

            map.put("data",resultSearchSignUpPromotionList);
            map.put("total",total);

        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }

        return ok(map);
    }

    /**
     * Update a new SignUpPromotion exist
     *
     * @param req UpdateSignUpPromotion contain info need to update SignUpPromotion
     * @return http status code
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<UpdateSignUpPromotion> req) {
        
        UpdateSignUpPromotion updateSignUpPromotion  = req.init();
        try {
            hdMiddleService.updateSignUpPromotion(updateSignUpPromotion);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException();
        }
        return ok();
    }
}
