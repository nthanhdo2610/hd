package com.tinhvan.hd.base;

import java.util.List;

public interface HDPagination<T> {

	public void next(List<T> ls);
	public Object next();

}
