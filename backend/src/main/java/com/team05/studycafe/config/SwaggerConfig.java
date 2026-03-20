package com.team05.studycafe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI studyCafeOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Smart Study Cafe API")
						.version("v0")
						.description("스마트 스터디카페 관리 웹서비스 백엔드 API 문서")
				)
				.servers(List.of(new Server().url("http://localhost:8080")));
	}
}
