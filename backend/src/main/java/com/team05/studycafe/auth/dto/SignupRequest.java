package com.team05.studycafe.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
		@NotBlank(message = "loginId는 필수입니다.")
		@Size(min = 4, max = 20, message = "loginId는 4자 이상 20자 이하여야 합니다.")
		String loginId,

		@NotBlank(message = "password는 필수입니다.")
		@Size(min = 8, max = 100, message = "password는 8자 이상이어야 합니다.")
		String password,

		@NotBlank(message = "name은 필수입니다.")
		@Size(max = 50, message = "name은 50자 이하여야 합니다.")
		String name,

		@NotBlank(message = "phone은 필수입니다.")
		@Pattern(regexp = "^[0-9-]{10,20}$", message = "phone 형식이 올바르지 않습니다.")
		String phone,

		@NotBlank(message = "email은 필수입니다.")
		@Email(message = "email 형식이 올바르지 않습니다.")
		String email
) {
}
