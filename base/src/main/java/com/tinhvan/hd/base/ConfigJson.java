package com.tinhvan.hd.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;

public class ConfigJson {

	private static ConfigJson instance;

	@JsonProperty("AESkey")
	private String AESKey;

	@JsonProperty("APIKey")
	private ArrayList<String> APIKey;

	public static ConfigJson getInstance() {
		if (instance == null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				File file = new ClassPathResource("application.json").getFile();
				Log.print("File:", file.getAbsolutePath());
				instance = mapper.readValue(file, ConfigJson.class);
			} catch (IOException e) {
				Log.print("Cannot get configuration", e.getMessage());
			}
			// mapper.writeValue(new File("./resources/application.json"), instance);
			// JSONParser jsonParser = new JSONParser();
		}
		return instance;
	}

	public ConfigJson() {
	}

	public String getAESKey() {
		return AESKey;
	}

	public void setAESKey(String v) {
		AESKey = v;
	}

	public ArrayList<String> getAPIKey() {
		return APIKey;
	}

	public void setAPIKey(ArrayList<String> v) {
		APIKey = new ArrayList<>(v);
	}

}
