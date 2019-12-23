package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.EmptyPayload;
import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.base.JWTPayload;
import com.tinhvan.hd.entity.Province;
import com.tinhvan.hd.service.ProvinceService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/province")
public class ProvinceController extends HDController {

    @Autowired
    private ProvinceService provinceService;

    /**
     * Find list province
     *
     * @param req
     * @return list of province
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllProvince(EmptyPayload req){

        List<Province> provinces;
        try {
            provinces = provinceService.getAllProvince();
        }catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(provinces);
    }
}
