package com.team05.studycafe.auth.dto;

import com.team05.studycafe.user.domain.User;
import com.team05.studycafe.user.domain.UserRole;
import com.team05.studycafe.user.domain.UserStatus;
import java.time.LocalDateTime;

public record SignupResponse(
		Long userId,
		String loginId,
		String name,
		String email,
		String phone,
		UserStatus userStatus,
		UserRole role,
		LocalDateTime createdAt
) {
	public static SignupResponse from(User user) {
		return new SignupResponse(
				user.getId(),
				user.getLoginId(),
				user.getName(),
				user.getEmail(),
				user.getPhone(),
				user.getUserStatus(),
				user.getRole(),
				user.getCreatedAt()
		);
	}
}
