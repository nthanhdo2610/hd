package com.tinhvan.hd.base;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import redis.clients.jedis.Jedis;

public class HDRedis extends Jedis {

	private static Map<UUID, HDRedis> instances = new HashMap<>();

	private HDRedis(String host, int port) {
		super(host, port);
	}

	private HDRedis(String host) {
		super(host);
	}

	public static HDRedis getInstance() {
		UUID requestId = HDServletRequest.getRequestId();
		return getInstance(requestId);
	}
	public static HDRedis getInstance(UUID requestId) {
		HDRedis redis = null;
		//Log.system("[HDRedis]", "START getInstance", requestId);
		if (!instances.containsKey(requestId)) {
			//Log.system("[HDRedis] create with requestId", requestId);
			try {
				Config c = HDConfig.getInstance();
				String[] host = c.getList("REDIS_HOST");
				if (host.length == 0 || host.length > 3) {
					throw new Exception("wronng redis host");
				}
				if (HDUtil.isNullOrEmpty(host[0])) {
					throw new Exception("empty host");
				}
				if (host.length > 1) {
					int port = Integer.parseInt(host[1]);
					redis = new HDRedis(host[0], port);
				} else {
					redis = new HDRedis(host[0]);
				}
				if (host.length > 2) {
					redis.auth(host[2]);
				}
				instances.put(requestId, redis);
			} catch (Exception ex) {
				Log.system("[HDRedis][Constructor]", ex.getMessage());
			}
		} else {
			redis = instances.get(requestId);
		}
		return redis;
	}
	public static void dispose() {
		UUID requestId = HDServletRequest.getRequestId();
		dispose(requestId);
	}

	public static void dispose(UUID requestId) {
		if (instances == null) {
			return;
		}
		//Log.info("[HDRedis] dispose with requestId", requestId);
		if (instances.containsKey(requestId)) {
			HDRedis redis = instances.get(requestId);
			if (redis != null) {
				redis.close();
			}
			instances.remove(requestId);
		}
	}

	public static void disposeAll() {
		instances.forEach((r, redis) -> {
			if (redis != null) {
				redis.close();
			}
		});
		instances.clear();
	}




}
