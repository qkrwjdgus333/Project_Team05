package com.team05.studycafe.auth.jwt;

import com.team05.studycafe.common.exception.CustomException;
import com.team05.studycafe.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;

	public JwtTokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(Long userId, String loginId, String role) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusMillis(jwtProperties.getAccessTokenExpirationMillis());

		return Jwts.builder()
				.subject(loginId)
				.claim("userId", userId)
				.claim("role", role)
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiresAt))
				.signWith(secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		parseClaims(token);
		return true;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		String loginId = claims.getSubject();
		String role = claims.get("role", String.class);

		if (!StringUtils.hasText(role)) {
			return new UsernamePasswordAuthenticationToken(loginId, null, List.of());
		}

		return new UsernamePasswordAuthenticationToken(
				loginId,
				null,
				List.of(new SimpleGrantedAuthority("ROLE_" + role))
		);
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (ExpiredJwtException ex) {
			throw new CustomException(ErrorCode.AUTH_TOKEN_EXPIRED);
		} catch (JwtException | IllegalArgumentException ex) {
			throw new CustomException(ErrorCode.AUTH_INVALID_TOKEN);
		}
	}
}
