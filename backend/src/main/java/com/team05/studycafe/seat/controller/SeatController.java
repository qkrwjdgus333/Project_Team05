package com.team05.studycafe.seat.controller;

import com.team05.studycafe.common.response.ApiResponse;
import com.team05.studycafe.seat.dto.SeatResponse;
import com.team05.studycafe.seat.service.SeatQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Seat", description = "좌석 조회 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

	private final SeatQueryService seatQueryService;

	public SeatController(SeatQueryService seatQueryService) {
		this.seatQueryService = seatQueryService;
	}

	@Operation(summary = "전체 좌석 조회", description = "좌석 번호, 상태, 사용 가능 여부를 전체 목록으로 조회합니다.")
	@GetMapping
	public ApiResponse<List<SeatResponse>> getSeats() {
		return ApiResponse.success(seatQueryService.getAllSeats());
	}

	@Operation(summary = "좌석 단건 조회", description = "좌석 번호로 좌석 정보를 조회합니다.")
	@GetMapping("/{seatNumber}")
	public ApiResponse<SeatResponse> getSeat(@PathVariable Integer seatNumber) {
		return ApiResponse.success(seatQueryService.getSeatByNumber(seatNumber));
	}
}
