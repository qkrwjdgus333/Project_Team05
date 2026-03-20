package com.team05.studycafe.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
		boolean success,
		T data,
		ErrorBody error,
		LocalDateTime timestamp
) {

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, data, null, LocalDateTime.now());
	}

	public static ApiResponse<Void> fail(String code, String message) {
		return new ApiResponse<>(false, null, new ErrorBody(code, message), LocalDateTime.now());
	}

	public record ErrorBody(String code, String message) {
	}
}
