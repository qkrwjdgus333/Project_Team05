package com.team05.studycafe.seat.dto;

import com.team05.studycafe.seat.domain.Seat;
import com.team05.studycafe.seat.domain.SeatStatus;

public record SeatResponse(
		Integer seatNumber,
		SeatStatus status,
		boolean available
) {
	public static SeatResponse from(Seat seat) {
		return new SeatResponse(
				seat.getSeatNumber(),
				seat.getStatus(),
				seat.isAvailable()
		);
	}
}
