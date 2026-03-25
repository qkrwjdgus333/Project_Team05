package com.team05.studycafe.auth.controller;

import com.team05.studycafe.auth.dto.LoginRequest;
import com.team05.studycafe.auth.dto.LoginResponse;
import com.team05.studycafe.auth.dto.SignupRequest;
import com.team05.studycafe.auth.dto.SignupResponse;
import com.team05.studycafe.auth.service.AuthService;
import com.team05.studycafe.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "회원가입/로그인 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "회원가입", description = "아이디 중복 검증 후 비밀번호를 암호화하여 회원을 생성합니다.")
	@PostMapping("/signup")
	public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
		return ApiResponse.success(authService.signup(request));
	}

	@Operation(summary = "로그인", description = "아이디/비밀번호를 검증하고 access token과 사용자 기본 정보를 반환합니다.")
	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		return ApiResponse.success(authService.login(request));
	}
}
