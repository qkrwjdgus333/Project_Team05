package com.team05.studycafe.auth.dto;

import com.team05.studycafe.user.domain.User;
import com.team05.studycafe.user.domain.UserRole;
import com.team05.studycafe.user.domain.UserStatus;

public record LoginResponse(
		Long userId,
		String loginId,
		String name,
		UserStatus userStatus,
		UserRole role,
		String accessToken
) {
	public static LoginResponse from(User user, String accessToken) {
		return new LoginResponse(
				user.getId(),
				user.getLoginId(),
				user.getName(),
				user.getUserStatus(),
				user.getRole(),
				accessToken
		);
	}
}
