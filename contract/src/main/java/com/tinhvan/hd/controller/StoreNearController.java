package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.EmptyPayload;
import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.dto.BankDTO;
import com.tinhvan.hd.dto.BankRequest;
import com.tinhvan.hd.dto.StoreDto;
import com.tinhvan.hd.dto.StoreRequest;
import com.tinhvan.hd.entity.Banks;
import com.tinhvan.hd.entity.StoreNearYou;
import com.tinhvan.hd.service.BankService;
import com.tinhvan.hd.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class StoreNearController extends HDController {

    @Autowired
    private StoreService storeService;

    /**
     * Find all store near customer
     *
     * @param req
     * @return list of store near result
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllStore(@RequestBody RequestDTO<EmptyPayload> req){

        List<StoreNearYou> storeNearYous = storeService.getAllStore(1);

        List<StoreDto> storeDtos = new ArrayList<>();

        if (storeNearYous != null && storeNearYous.size() > 0) {
            for (StoreNearYou nearYou : storeNearYous) {
                StoreDto storeDto = new StoreDto();
                storeDto.setAddress(nearYou.getAddress());
                storeDto.setDistrictCode(nearYou.getDistrictCode());
                storeDto.setName(nearYou.getName());
                storeDto.setProvinceCode(nearYou.getProvinceCode());
                storeDtos.add(storeDto);
            }
        }

        return ok(storeDtos);
    }

    /**
     * Save a new Store
     *
     * @param req StoreRequest contain info store need to save
     * @return http status coode
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveStore(@RequestBody RequestDTO<StoreRequest> req){

        StoreRequest request = req.init();

        List<StoreDto> storeDtos = request.getData();
        List<StoreNearYou> list = new ArrayList<>();

        if (storeDtos != null && storeDtos.size() > 0) {
            for (StoreDto dto : storeDtos) {
                StoreNearYou storeNearYou = new StoreNearYou();
                storeNearYou.setAddress(dto.getAddress());
                storeNearYou.setName(dto.getName());
                storeNearYou.setCreatedAt(new Date());
                list.add(storeNearYou);
            }
        }

        if (list != null && list.size() > 0) {
            storeService.saveAll(list);
        }
        return ok(null);
    }
}
