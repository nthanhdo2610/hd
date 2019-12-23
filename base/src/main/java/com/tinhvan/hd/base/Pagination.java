package com.tinhvan.hd.base;

import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("rawtypes")
public class Pagination<R extends HDPagination> implements HDPayload {

	@SerializedName("cursor")
	private String cursor;

	@SerializedName("filter")
	private R filter;

	public void validatePayload() {
		if (filter == null) {
			throw new BadRequestException("null filter");
		}
	}

	public Pagination() {
	}

	public R filter(Class<R> clazz) {
		try {
			if (!HDUtil.isNullOrEmpty(cursor)) {
				byte[] decodedBytes = Base64.getDecoder().decode(cursor);
				String json = new String(decodedBytes);
				Gson gson = HDUtil.gson();
				this.filter = (R)gson.fromJson(json, clazz);
			}
		} catch (Exception ex) {
			throw new BadRequestException("invalid cursor");
		}
		return this.filter;
	}

	public R getFilter() {
		return this.filter;
	}

	public void setFilter(R filter) {
		this.filter = filter;
	}

	public String getCursor() {
		return this.cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

	@SuppressWarnings("unchecked")
	public PaginationResponse response(List<?> ls) {
		PaginationResponse res = new PaginationResponse();
		res.setEntities(ls);
		if (ls.size() > 0) {
			filter.next(ls);
			res.setCursor(cursor());
		}
		return res;
	}

	public String cursor() {
		Gson gson = HDUtil.gson();
		String json = gson.toJson(filter, filter.getClass());
		return new String(Base64.getEncoder().encode(json.getBytes()));
	}

}
