package com.team05.studycafe.system.dto;

import java.time.LocalDateTime;

public record SystemHealthResponse(
		String serviceName,
		String status,
		LocalDateTime serverTime
) {
}
