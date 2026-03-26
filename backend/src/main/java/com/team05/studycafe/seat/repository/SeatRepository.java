package com.team05.studycafe.seat.repository;

import com.team05.studycafe.seat.domain.Seat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	Optional<Seat> findBySeatNumber(Integer seatNumber);
}
