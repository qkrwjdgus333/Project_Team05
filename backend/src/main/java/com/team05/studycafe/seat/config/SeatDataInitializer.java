package com.team05.studycafe.seat.config;

import com.team05.studycafe.seat.domain.Seat;
import com.team05.studycafe.seat.domain.SeatStatus;
import com.team05.studycafe.seat.repository.SeatRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeatDataInitializer implements CommandLineRunner {

	private final SeatRepository seatRepository;

	@Value("${app.seed.seats.enabled:true}")
	private boolean seedEnabled;

	@Value("${app.seed.seats.count:30}")
	private int seatCount;

	public SeatDataInitializer(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

	@Override
	public void run(String... args) {
		if (!seedEnabled) {
			return;
		}

		if (seatRepository.count() > 0) {
			return;
		}

		List<Seat> seats = IntStream.rangeClosed(1, seatCount)
				.mapToObj(number -> Seat.create(number, SeatStatus.AVAILABLE))
				.toList();
		seatRepository.saveAll(seats);
	}
}
