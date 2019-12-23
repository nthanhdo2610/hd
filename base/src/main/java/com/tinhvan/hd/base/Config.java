package com.tinhvan.hd.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

// import javax.persistence.EntityManager;
// import javax.persistence.EntityManagerFactory;
// import org.springframework.beans.factory.annotation.Autowired;

@Component
public class Config {

    // @Autowired
    // private EntityManagerFactory entityManagerFactory;
    public static Map<String, String[]> map;
    @Autowired
    private CacheServeService cacheServeService;

    // @SuppressWarnings("unchecked")
    public Config load() {
        map = cacheServeService.getDataFromCache(HDConstant.SYSTEM_CONFIG);
//        if (map == null) {
//            map = new HashMap<>();
//            // EntityManager em = HDEntityManager.getInstance();
//            DAO.query((em) -> {
//                Query q = em.createQuery("FROM ConfigEntity");
//                for (Object obj : q.getResultList()) {
//                    ConfigEntity entity = (ConfigEntity) obj;
//                    map.put(entity.getKey(), entity.getValue());
//                }
//            });
//        }
        return this;
    }

    public String get(String key) {
        if (map == null) return "";
        String[] data = map.get(key);
        return data == null || data.length == 0 ? "" : data[0];
    }

    public String[] getList(String key) {
        if (map == null) return null;
        String[] data = map.get(key);
        return data == null || data.length == 0 ? null : data;
    }

    public void clear() {
        this.map = null;
        this.load();
    }

}
