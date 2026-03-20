package com.team05.studycafe.system.controller;

import com.team05.studycafe.common.response.ApiResponse;
import com.team05.studycafe.system.dto.SystemEchoRequest;
import com.team05.studycafe.system.dto.SystemEchoResponse;
import com.team05.studycafe.system.dto.SystemHealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System", description = "시스템 기본 상태 확인 API")
@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

	@Operation(summary = "서버 상태 확인", description = "백엔드 서버 기본 상태를 확인합니다.")
	@GetMapping("/health")
	public ApiResponse<SystemHealthResponse> health() {
		SystemHealthResponse response = new SystemHealthResponse(
				"studycafe-backend",
				"UP",
				LocalDateTime.now()
		);
		return ApiResponse.success(response);
	}

	@Operation(summary = "에코 테스트", description = "요청 메시지를 그대로 반환해 공통 응답/검증 동작을 확인합니다.")
	@PostMapping("/echo")
	public ApiResponse<SystemEchoResponse> echo(@Valid @RequestBody SystemEchoRequest request) {
		return ApiResponse.success(new SystemEchoResponse(request.message()));
	}
}
