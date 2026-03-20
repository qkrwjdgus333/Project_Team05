package com.team05.studycafe.system.dto;

import jakarta.validation.constraints.NotBlank;

public record SystemEchoRequest(
		@NotBlank(message = "message는 비어 있을 수 없습니다.")
		String message
) {
}
