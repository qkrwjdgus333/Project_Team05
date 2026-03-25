package com.team05.studycafe.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		@NotBlank(message = "loginId는 필수입니다.")
		@Size(min = 4, max = 20, message = "loginId는 4자 이상 20자 이하여야 합니다.")
		String loginId,

		@NotBlank(message = "password는 필수입니다.")
		@Size(min = 8, max = 100, message = "password는 8자 이상이어야 합니다.")
		String password
) {
}
