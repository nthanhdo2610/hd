package com.tinhvan.hd.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HDAuthorize {

	private static AuthorizeUser instance;

	@Autowired
	public HDAuthorize(AuthorizeUser authorizeUser) {
		HDAuthorize.instance = authorizeUser;
	}

	public static List<String> getInstance(String path) {
		return instance.load(path);
	}

}
