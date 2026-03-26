package com.team05.studycafe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team05.studycafe.auth.jwt.JwtAuthenticationFilter;
import com.team05.studycafe.common.exception.ErrorCode;
import com.team05.studycafe.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			ObjectMapper objectMapper,
			ObjectProvider<JwtAuthenticationFilter> jwtAuthenticationFilterProvider
	) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/swagger-ui/**",
								"/v3/api-docs/**",
								"/api/v1/system/**",
								"/api/v1/auth/signup",
								"/api/v1/auth/login"
						).permitAll()
						.requestMatchers("/api/v1/seats/**").authenticated()
						.anyRequest().authenticated()
				)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint((request, response, authException) ->
								writeErrorResponse(response, objectMapper, ErrorCode.COMMON_UNAUTHORIZED))
						.accessDeniedHandler((request, response, accessDeniedException) ->
								writeErrorResponse(response, objectMapper, ErrorCode.COMMON_FORBIDDEN))
				)
				.cors(Customizer.withDefaults());

		JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilterProvider.getIfAvailable();
		if (jwtAuthenticationFilter != null) {
			http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		}

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private void writeErrorResponse(HttpServletResponse response, ObjectMapper objectMapper, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		objectMapper.writeValue(response.getWriter(), ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
	}
}
