package com.tinhvan.hd.base;

import com.tinhvan.hd.base.enities.AuthorizeUserEntity;
import com.tinhvan.hd.base.enities.ConfigEntity;
import com.tinhvan.hd.base.enities.RoleEntity;
import com.tinhvan.hd.base.repository.AuthorizeUserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorizeUser {

	@Autowired
	private CacheServeService cacheServeService;

	private List<Long> roles;

	public List<Long> load(String path) {

		roles = cacheServeService.getDataRolesByPathFromCache(path);
//		if (roles == null) {
//			roles = new ArrayList<>();


//			DAO.query((em) -> {
//				Query q = em.createQuery("FROM AuthorizeUserEntity where path= : path");
//				q.setParameter("path",path);
//				for (Object obj : q.getResultList()) {
//					AuthorizeUserEntity entity = (AuthorizeUserEntity)obj;
//
//				}
//			});

//		}
		return roles;
	}


//	public String[] getList(String key) {
//		if (map == null) return null;
//		String[] data = map.get(key);
//		return data == null || data.length == 0 ? null : data;
//	}

}
