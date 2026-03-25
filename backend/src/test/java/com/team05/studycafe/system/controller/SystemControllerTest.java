package com.team05.studycafe.system.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team05.studycafe.auth.jwt.JwtAuthenticationFilter;
import com.team05.studycafe.common.exception.GlobalExceptionHandler;
import com.team05.studycafe.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
		controllers = SystemController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class SystemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test//성공 테스트용
	@DisplayName("성공: 서버 상태 확인 API가 공통 응답 형태로 반환된다.")
	void health_success() throws Exception {
		mockMvc.perform(get("/api/v1/system/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.serviceName").value("studycafe-backend"))
				.andExpect(jsonPath("$.data.status").value("UP"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test//실패테스트용
	@DisplayName("실패: 에코 API에서 message가 비어 있으면 400 에러를 반환한다.")
	void echo_fail_when_message_is_blank() throws Exception {
		String requestBody = objectMapper.writeValueAsString(new TestEchoRequest(""));

		mockMvc.perform(post("/api/v1/system/echo")
						.contentType(APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("COMMON-400"))
				.andExpect(jsonPath("$.error.message").value("message는 비어 있을 수 없습니다."))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	@DisplayName("실패: 에코 API에 Content-Type이 text/plain이면 415 에러를 반환한다.")
	void echo_fail_when_content_type_is_text_plain() throws Exception {
		mockMvc.perform(post("/api/v1/system/echo")
						.contentType("text/plain")
						.content("{\"message\":\"hello\"}"))
				.andExpect(status().isUnsupportedMediaType())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("COMMON-415"))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	private record TestEchoRequest(String message) {
	}
}
