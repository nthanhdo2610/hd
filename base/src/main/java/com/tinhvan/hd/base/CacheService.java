package com.tinhvan.hd.base;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import com.tinhvan.hd.base.enities.ConfigEntity;
import com.tinhvan.hd.base.repository.AuthorizeUserEntityRepository;
import com.tinhvan.hd.base.repository.ConfigEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CacheService {
    private final static Logger LOG = LoggerFactory.getLogger(Log.class);
    private Map<String, String[]> map;

    private List<Long> roles;

    @Autowired
    private ConfigEntityRepository configEntityRepository;

    @Autowired
    private AuthorizeUserEntityRepository authorizeUserEntityRepository;

    @Cacheable(cacheNames = "testCache", key = "'myPrefix_'.concat(#key)")
    public Map<String, String[]> getFromCache(String key) {
        return null;
    }

    @CachePut(cacheNames = "testCache", key = "'myPrefix_'.concat(#key)")
    public Map<String, String[]> populateCache(String key) {
        map = new HashMap<>();
        List<ConfigEntity> configEntityList = configEntityRepository.findAll();
        for (ConfigEntity configEntity : configEntityList) {
            map.put(configEntity.getKey(), configEntity.getValue());
        }
        LOG.info("Returning NOT from cache!");
        return map;
    }

    // roles

    @Cacheable(cacheNames = "testCache", key = "'myPrefix_'.concat(#key)")
    public List<Long> getRolesFromCache(String key) {
        return null;
    }

    @CachePut(cacheNames = "testCache", key = "'myPrefix_'.concat(#key)")
    public List<Long> populateRolesCache(String key) {
        roles = new ArrayList<>();
        List<AuthorizeUserEntity> userEntities = authorizeUserEntityRepository.findAllByPath(key);

        if (userEntities != null && userEntities.size() > 0) {
            for (AuthorizeUserEntity entity : userEntities) {
                roles.add(entity.getRoleId());
            }
        }
        LOG.info("Returning NOT from cache!");
        return roles;
    }

    @CacheEvict(cacheNames = "testCache", key = "'myPrefix_'.concat(#key)")
    public void forgetAboutThis(String key) {
        LOG.info("Forgetting everything about this!");
    }

}
