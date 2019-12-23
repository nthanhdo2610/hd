package com.tinhvan.hd.base;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HDEntityManager {

	private static EMF instance;

	private static Map<UUID, EntityManager> instances = new HashMap<>();

	@Autowired
	public HDEntityManager(EMF emf) {
		HDEntityManager.instance = emf;
	}

	public static EntityManager getInstance() {
		UUID requestId = HDServletRequest.getRequestId();
		return getInstance(requestId);
	}

	public static EntityManager getInstance(UUID requestId) {
		EntityManager em = null;
		//Log.print("[HDEM]", "START getInstance", requestId);
		if (!instances.containsKey(requestId)) {
			try {
				em = instance.getEMF().createEntityManager();
				instances.put(requestId, em);
			} catch (Exception ex) {
				Log.print("[HDEntityManager][getInstance]", ex.getMessage());
			}
		} else {
			em = instances.get(requestId);
		}
		return em;
	}
	public static void dispose() {
		UUID requestId = HDServletRequest.getRequestId();
		dispose(requestId);
	}
	public static void dispose(UUID requestId) {
		if (instances == null) {
			return;
		}
		//Log.print("[HDEM]", "STOP getInstance", requestId);
		if (instances.containsKey(requestId)) {
			EntityManager em = instances.get(requestId);
			if (em != null) {
				em.close();
			}
			instances.remove(requestId);
		}
	}

	public static void disposeAll() {
		if (instances == null) {
			return;
		}
		instances.forEach((r, em) -> {
			if (em != null) {
				em.close();
			}
		});
		instances.clear();
	}


}
