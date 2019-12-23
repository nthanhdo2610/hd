package com.tinhvan.hd.base;

import javax.persistence.EntityManager;

public interface HDQuery {
	void execute(EntityManager session);
}
