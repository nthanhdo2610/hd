package com.tinhvan.hd.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HDConfig {

    private static Config instance;

    @Autowired
    public HDConfig(Config config) {
        HDConfig.instance = config;
    }

    public static Config getInstance() {
        return instance.load();
    }

    public static void clearHdRedis() {
        instance.clear();
    }

}
