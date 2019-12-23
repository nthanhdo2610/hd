package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.*;
import com.tinhvan.hd.dto.ProductionRequest;
import com.tinhvan.hd.entity.Production;
import com.tinhvan.hd.service.ProductionService;
import com.tinhvan.hd.utils.ContractUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/v1/production")
public class ProductionController extends HDController {

    @Autowired
    private ProductionService productionService;

    /**
     * Find list production by type
     *
     * @param req IdPayload contain type request
     * @return list of production
     */
    @PostMapping("/list")
    public ResponseEntity<?> getAllProduction(@RequestBody RequestDTO<IdPayload> req){
        IdPayload idPayload = req.init();
        String type = (String) idPayload.getId();
        List<Production> productions = new ArrayList<>();
        try {
            productions = productionService.getAllProductionByType(type);
        }catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(productions);
    }

    /**
     * Save a production
     *
     * @param req ProductionRequest contain info of production request
     * @return http status code
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveProduction(@RequestBody RequestDTO<ProductionRequest> req){
        ProductionRequest productionRequest = req.init();
        String productionNames = productionRequest.getProductionNames();

        List<String> list = new ArrayList<String>(Arrays.asList(productionNames.split(",")));
        List<Production> productions = new ArrayList<>();
        try {
            if (list != null) {
                for (String productionName : list) {
                    Production production = new Production();
                    production.setStatus(1);
                    production.setCreatedAt(new Date());
                    production.setProductionName(productionName);
                    production.setType(productionRequest.getType());
                    productions.add(production);
                }
                productionService.saveAll(productions);
            }
        }catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(null);
    }
}
