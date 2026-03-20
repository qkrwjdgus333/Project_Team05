package com.team05.studycafe.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team05.studycafe.auth.dto.SignupRequest;
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
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("성공: 유효한 회원가입 요청이면 사용자가 생성되고 비밀번호는 암호화되어 저장된다.")
	void signup_success() throws Exception {
		SignupRequest request = new SignupRequest("hong1234", "password123!", "홍길동", "010-1234-5678", "hong@test.com");

		mockMvc.perform(post("/api/v1/auth/signup")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.loginId").value("hong1234"))
				.andExpect(jsonPath("$.data.userStatus").value("NORMAL"))
				.andExpect(jsonPath("$.data.role").value("USER"))
				.andExpect(jsonPath("$.timestamp").exists());

		User savedUser = userRepository.findByLoginId("hong1234").orElseThrow();
		assertThat(savedUser.getPassword()).isNotEqualTo("password123!");
		assertThat(passwordEncoder.matches("password123!", savedUser.getPassword())).isTrue();
	}

	@Test
	@DisplayName("실패: 이미 존재하는 loginId로 회원가입하면 중복 예외가 발생한다.")
	void signup_fail_when_login_id_is_duplicated() throws Exception {
		User existingUser = User.create("dupUser", passwordEncoder.encode("password123!"), "기존회원", "010-9999-9999", "dup@test.com");
		userRepository.save(existingUser);

		SignupRequest duplicatedRequest = new SignupRequest("dupUser", "password123!", "신규회원", "010-1234-5678", "new@test.com");

		mockMvc.perform(post("/api/v1/auth/signup")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(duplicatedRequest)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("USER-409"))
				.andExpect(jsonPath("$.error.message").value("이미 사용 중인 아이디입니다."));
	}

	@Test
	@DisplayName("실패: email 형식이 올바르지 않으면 validation 예외가 발생한다.")
	void signup_fail_when_email_is_invalid() throws Exception {
		SignupRequest invalidRequest = new SignupRequest("validId1", "password123!", "홍길동", "010-1234-5678", "wrong-email");

		mockMvc.perform(post("/api/v1/auth/signup")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("COMMON-400"))
				.andExpect(jsonPath("$.timestamp").exists());
	}
}
