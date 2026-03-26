package com.team05.studycafe.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	COMMON_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
	COMMON_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-401", "인증이 필요합니다."),
	COMMON_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-403", "접근 권한이 없습니다."),
	COMMON_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON-405", "지원하지 않는 HTTP 메서드입니다."),
	COMMON_UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON-415", "지원하지 않는 Content-Type 입니다."),
	COMMON_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-404", "요청한 리소스를 찾을 수 없습니다."),
	SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT-404", "요청한 좌석을 찾을 수 없습니다."),
	AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401", "유효하지 않은 토큰입니다."),
	AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-402", "만료된 토큰입니다."),
	USER_LOGIN_ID_DUPLICATE(HttpStatus.CONFLICT, "USER-409", "이미 사용 중인 아이디입니다."),
	USER_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "USER-401", "아이디 또는 비밀번호가 올바르지 않습니다."),
	COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
