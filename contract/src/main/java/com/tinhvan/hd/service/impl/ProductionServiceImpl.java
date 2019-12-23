package com.tinhvan.hd.service.impl;
import com.tinhvan.hd.entity.Production;
import com.tinhvan.hd.repository.ProductionRepository;
import com.tinhvan.hd.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private ProductionRepository productionRepository;

    @Override
    public List<Production> getAllProductionByType(String type) {
        return productionRepository.findAllByType(type);
        /*if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Production>();
        }*/
    }

    @Override
    public void saveAll(List<Production> productions) {
        productionRepository.saveAll(productions);
    }
}
