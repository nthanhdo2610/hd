package com.tinhvan.hd.filehandler.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseDTO<T> {

	@JsonProperty(value="code", required=true)
	private int code;

	@JsonProperty(value="payload")
	private T payload;

	public ResponseDTO() {
	}

	public ResponseDTO(int code) {
		this.code = code;
	}

	public ResponseDTO(int code, T payload) {
		this.code = code;
		this.payload = payload;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return code == 200;
	}

	@Override
	public String toString() {
		String result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.writeValueAsString(this);

		} catch (Exception ex) {}
		return result;
	}

}
