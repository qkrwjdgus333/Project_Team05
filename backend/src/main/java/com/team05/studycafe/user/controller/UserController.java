package com.team05.studycafe.user.controller;

import com.team05.studycafe.common.response.ApiResponse;
import com.team05.studycafe.user.dto.UserMeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 인증 정보 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Operation(summary = "내 인증 정보 조회", description = "Access token 기반으로 현재 로그인 사용자의 식별값을 반환합니다.")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/me")
	public ApiResponse<UserMeResponse> me(Authentication authentication) {
		return ApiResponse.success(new UserMeResponse(authentication.getName()));
	}
}
