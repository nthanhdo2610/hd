package com.tinhvan.hd.base;

import java.util.List;

public class PaginationResponse {

	private String cursor;
	private List<?> entities;

	public PaginationResponse() {
	}

	public String getCursor() {
		return this.cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	public List<?> getEntities() {
		return this.entities;
	}

	public void setEntities(List<?> entities) {
		this.entities = entities;
	}

}
