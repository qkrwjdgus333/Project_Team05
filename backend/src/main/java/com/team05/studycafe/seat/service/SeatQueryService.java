package com.team05.studycafe.seat.service;

import com.team05.studycafe.common.exception.CustomException;
import com.team05.studycafe.common.exception.ErrorCode;
import com.team05.studycafe.seat.dto.SeatResponse;
import com.team05.studycafe.seat.repository.SeatRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeatQueryService {

	private final SeatRepository seatRepository;

	public SeatQueryService(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

	public List<SeatResponse> getAllSeats() {
		return seatRepository.findAll(Sort.by(Sort.Direction.ASC, "seatNumber")).stream()
				.map(SeatResponse::from)
				.toList();
	}

	public SeatResponse getSeatByNumber(Integer seatNumber) {
		return seatRepository.findBySeatNumber(seatNumber)
				.map(SeatResponse::from)
				.orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));
	}
}
