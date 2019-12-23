package com.tinhvan.hd.base;

import javax.persistence.EntityManager;

import java.io.Serializable;

public abstract class HDDAO<T, ID extends Serializable> {

	public T find(Class<T> type, Serializable id) {
		EntityManager em = HDEntityManager.getInstance();
		T entity = em.find(type, id);
		return entity;
	}

	public T insert(T entity) {
		EntityManager em = HDEntityManager.getInstance();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch(Exception ex) {
			em.getTransaction().rollback();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return entity;
	}

	public T update(T entity) {
		EntityManager em = HDEntityManager.getInstance();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		} catch(Exception ex) {
			em.getTransaction().rollback();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return entity;
	}

	public boolean delete(T entity) {
		EntityManager em = HDEntityManager.getInstance();
		try {
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
			return true;
		} catch(Exception ex) {
			em.getTransaction().rollback();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return false;
	}


	public boolean delete(Class<T> type, Serializable id) {
		EntityManager em = HDEntityManager.getInstance();
		try {
			em.getTransaction().begin();
			T entity = em.find(type, id);
			if (entity == null) {
				return false;
			}
			em.remove(entity);
			em.getTransaction().commit();
			return true;
		} catch(Exception ex) {
			em.getTransaction().rollback();
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return false;
	}

}
