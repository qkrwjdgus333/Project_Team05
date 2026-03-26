package com.team05.studycafe.seat.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team05.studycafe.auth.dto.LoginRequest;
import com.team05.studycafe.seat.domain.Seat;
import com.team05.studycafe.seat.domain.SeatStatus;
import com.team05.studycafe.seat.repository.SeatRepository;
import com.team05.studycafe.user.domain.User;
import com.team05.studycafe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SeatControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		seatRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("성공: 전체 좌석 조회 시 좌석 번호/상태/사용 가능 여부를 반환한다.")
	void get_seats_success() throws Exception {
		seatRepository.save(Seat.create(1, SeatStatus.AVAILABLE));
		seatRepository.save(Seat.create(2, SeatStatus.OCCUPIED));
		String accessToken = issueAccessToken("seatUser1", "password123!");

		mockMvc.perform(get("/api/v1/seats")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.length()").value(2))
				.andExpect(jsonPath("$.data[0].seatNumber").value(1))
				.andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))
				.andExpect(jsonPath("$.data[0].available").value(true))
				.andExpect(jsonPath("$.data[1].seatNumber").value(2))
				.andExpect(jsonPath("$.data[1].status").value("OCCUPIED"))
				.andExpect(jsonPath("$.data[1].available").value(false));
	}

	@Test
	@DisplayName("실패: 존재하지 않는 좌석 번호 조회 시 404 에러를 반환한다.")
	void get_seat_fail_when_not_found() throws Exception {
		String accessToken = issueAccessToken("seatUser2", "password123!");

		mockMvc.perform(get("/api/v1/seats/99")
						.header("Authorization", "Bearer " + accessToken))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("SEAT-404"))
				.andExpect(jsonPath("$.error.message").value("요청한 좌석을 찾을 수 없습니다."))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	private String issueAccessToken(String loginId, String password) throws Exception {
		User user = User.create(loginId, passwordEncoder.encode(password), "좌석테스트회원", "010-1111-1111", loginId + "@test.com");
		userRepository.save(user);

		LoginRequest loginRequest = new LoginRequest(loginId, password);
		String loginResponseBody = mockMvc.perform(post("/api/v1/auth/login")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return objectMapper.readTree(loginResponseBody).path("data").path("accessToken").asText();
	}
}
