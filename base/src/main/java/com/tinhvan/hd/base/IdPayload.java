package com.tinhvan.hd.base;

public class IdPayload<T> implements HDPayload {

	private T id;

	@Override
	public void validatePayload() {
		if (id == null) {
			throw new BadRequestException(1106,"invalid id");
		}
	}

	public T getId() {
		return this.id;
	}

	public void setId(T id) {
		this.id = id;
	}


}
