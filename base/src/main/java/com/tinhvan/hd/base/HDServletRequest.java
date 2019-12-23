package com.tinhvan.hd.base;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HDServletRequest {

	public static HttpServletRequest get() {
		ServletRequestAttributes ra =
				((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		return ra.getRequest();
	}

	public static UUID getRequestId() {
		HttpServletRequest req = get();
		UUID requestId = (UUID)req.getAttribute("requestId");
		if (requestId == null) {
			requestId = UUID.randomUUID();
			req.setAttribute("requestId", requestId);
		}
		return requestId;
	}

	public static String getApiKey() {
		HttpServletRequest req = get();
		return req.getHeader("x-api-key");
	}

	public static JWTPayload getJWTPayload() {
		HttpServletRequest req = get();
		return (JWTPayload)req.getAttribute("JWT");
	}

	public static UUID getXRequestId() {
		HttpServletRequest req = get();
		UUID xRequestId = null;
		String requestId = req.getHeader("x-request-id");
		if (!HDUtil.isNullOrEmpty(requestId)) {
			try {
				xRequestId = UUID.fromString(requestId);
			} catch (Exception ex) {}
		}
		return xRequestId;
	}

}
