package com.tinhvan.hd.base;

import com.tinhvan.hd.base.enities.RoleEntity;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

@Component
public class Role {

	private Map<String, String> map;

	public Role load() {
		if (map == null) {
			map = new HashMap<>();
			DAO.query((em) -> {
				Query q = em.createQuery("FROM RoleEntity");
				for (Object obj : q.getResultList()) {
					RoleEntity entity = (RoleEntity)obj;
					map.put(entity.getRole(),entity.getId().toString());
				}
			});
		}
		return this;
	}

	public int get(String role) {
		if (map == null) return 0;
		int id = 0;
		String data = map.get(role);
		if(HDUtil.isNullOrEmpty(data)){
			data = "0";
		}
		id = Integer.parseInt(data);
		return id;
	}


}
