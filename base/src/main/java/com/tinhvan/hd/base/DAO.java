package com.tinhvan.hd.base;

import javax.persistence.EntityManager;

public class DAO {

	public static void query(HDQuery... queries) {
		EntityManager em = HDEntityManager.getInstance();
		try {
			em.getTransaction().begin();
			for (HDQuery query : queries) {
				query.execute(em);
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
			throw new InternalServerErrorException(ex.getMessage());
		}
	}

}
