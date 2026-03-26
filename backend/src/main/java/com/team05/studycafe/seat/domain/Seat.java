package com.team05.studycafe.seat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Integer seatNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private SeatStatus status;

	protected Seat() {
	}

	private Seat(Integer seatNumber, SeatStatus status) {
		this.seatNumber = seatNumber;
		this.status = status;
	}

	public static Seat create(Integer seatNumber, SeatStatus status) {
		return new Seat(seatNumber, status);
	}

	public Long getId() {
		return id;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public SeatStatus getStatus() {
		return status;
	}

	public boolean isAvailable() {
		return SeatStatus.AVAILABLE == status;
	}
}
