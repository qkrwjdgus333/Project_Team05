package com.team05.studycafe.auth.service;

import com.team05.studycafe.auth.dto.LoginRequest;
import com.team05.studycafe.auth.dto.LoginResponse;
import com.team05.studycafe.auth.dto.SignupRequest;
import com.team05.studycafe.auth.dto.SignupResponse;
import com.team05.studycafe.auth.jwt.JwtTokenProvider;
import com.team05.studycafe.common.exception.CustomException;
import com.team05.studycafe.common.exception.ErrorCode;
import com.team05.studycafe.user.domain.User;
import com.team05.studycafe.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByLoginId(request.loginId())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_LOGIN_FAILED));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new CustomException(ErrorCode.USER_LOGIN_FAILED);
		}

		String accessToken = jwtTokenProvider.createAccessToken(
				user.getId(),
				user.getLoginId(),
				user.getRole().name()
		);

		return LoginResponse.from(user, accessToken);
	}

	@Transactional
	public SignupResponse signup(SignupRequest request) {
		if (userRepository.existsByLoginId(request.loginId())) {
			throw new CustomException(ErrorCode.USER_LOGIN_ID_DUPLICATE);
		}

		String encodedPassword = passwordEncoder.encode(request.password());
		User user = User.create(
				request.loginId(),
				encodedPassword,
				request.name(),
				request.phone(),
				request.email()
		);

		try {
			User savedUser = userRepository.save(user);
			return SignupResponse.from(savedUser);
		} catch (DataIntegrityViolationException ex) {
			throw new CustomException(ErrorCode.USER_LOGIN_ID_DUPLICATE);
		}
	}
}
