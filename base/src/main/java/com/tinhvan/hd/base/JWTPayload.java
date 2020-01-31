package com.tinhvan.hd.base;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JWTPayload {

    private UUID uuid;
    private String role;

    private long createdAt;
    private long lastModifyPassword;
    private String environment;

    public JWTPayload() {
        role = "";
        createdAt = 0;
    }

    public JWTPayload(UUID uuid, String role, long createdAt,
                      long lastModifyPassword, String environment) {
        this.uuid = uuid;
        this.role = role;
        this.createdAt = createdAt;
        this.lastModifyPassword = lastModifyPassword;
        this.environment = environment;
    }
	/*
	public JWTPayload(UUID uuid, int role) {
		this.uuid = uuid;
		this.role = role;
		Config c = HDConfig.getInstance();
		long expiredTime = HDUtil.getUnixTime();
		try {
			Long addTime = Long.valueOf(c.get("JWT_EXPIRED_TIME"));
			if (addTime != null) {
				expiredTime += addTime;
			}
		} catch (Exception ex) {
			Log.error("[JWTPayload]", "JWT_EXPIRED_TIME error", ex.getMessage());
		}
		this.expiredAt = expiredTime;
	}
	*/

    @JsonIgnore
    public boolean isValid() {
        long now = HDUtil.getUnixTimeNow();
        return this.uuid != null && !HDUtil.isNullOrEmpty(this.role)
                && this.createdAt <= now && !HDUtil.isNullOrEmpty(this.environment);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastModifyPassword() {
        return lastModifyPassword;
    }

    public void setLastModifyPassword(long lastModifyPassword) {
        this.lastModifyPassword = lastModifyPassword;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
