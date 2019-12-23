package com.tinhvan.hd.service;

import com.tinhvan.hd.entity.Production;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductionService {

    List<Production> getAllProductionByType(String type);

    void saveAll(List<Production> productions);
}
