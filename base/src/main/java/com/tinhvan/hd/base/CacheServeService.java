package com.tinhvan.hd.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
public class CacheServeService {
    private final static Logger LOG = LoggerFactory.getLogger(Log.class);
    private Map<String, String[]> map;


    @Autowired
    private CacheService cacheService;

    public Map getDataFromCache(String key) {
        Map map;
        map = cacheService.getFromCache(key);
        if (ObjectUtils.isEmpty(map)) {
            map = cacheService.populateCache(key);
        }
        return map;
    }

    public List<Long> getDataRolesByPathFromCache(String key) {
        List<Long> roles;
        roles = cacheService.getRolesFromCache(key);
        if (ObjectUtils.isEmpty(roles)) {
            roles = cacheService.populateRolesCache(key);
        }
        return roles;
    }
}
