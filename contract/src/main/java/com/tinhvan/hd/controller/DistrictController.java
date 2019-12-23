package com.tinhvan.hd.controller;
import com.tinhvan.hd.base.*;
import com.tinhvan.hd.entity.District;
import com.tinhvan.hd.service.DistrictService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/district")
public class DistrictController extends HDController {

    @Autowired
    private DistrictService districtService;

    /**
     * Find list district by province id
     *
     * @param req IdPayload contain province id
     * @return list of district
     */
    @PostMapping("/list")
    public ResponseEntity<?> findDistrict(@RequestBody RequestDTO<IdPayload> req){
        IdPayload idPayload = req.init();
        Integer provinceId = (Integer) idPayload.getId();
        List<District> districts;
        try {
            districts = districtService.getAllDistrictByProvinceId(provinceId);
        }catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(districts);
    }
}
