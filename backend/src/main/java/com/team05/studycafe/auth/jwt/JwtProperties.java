package com.team05.studycafe.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String secret;
	private long accessTokenExpirationMillis;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getAccessTokenExpirationMillis() {
		return accessTokenExpirationMillis;
	}

	public void setAccessTokenExpirationMillis(long accessTokenExpirationMillis) {
		this.accessTokenExpirationMillis = accessTokenExpirationMillis;
	}
}
