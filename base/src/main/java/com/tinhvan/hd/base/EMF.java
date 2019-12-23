package com.tinhvan.hd.base;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EMF {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public EntityManagerFactory getEMF() {
		return entityManagerFactory;
	}

}
