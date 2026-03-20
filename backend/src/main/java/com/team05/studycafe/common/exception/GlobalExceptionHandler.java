package com.team05.studycafe.common.exception;

import com.team05.studycafe.common.response.ApiResponse;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
				.body(ApiResponse.fail(errorCode.getCode(), ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
		FieldError firstFieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
		String message = Objects.nonNull(firstFieldError) ? firstFieldError.getDefaultMessage() : ErrorCode.COMMON_BAD_REQUEST.getMessage();
		return ResponseEntity.status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_BAD_REQUEST.getCode(), message));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(ErrorCode.COMMON_BAD_REQUEST.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_BAD_REQUEST.getCode(), "요청 본문 형식이 올바르지 않습니다."));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
		return ResponseEntity.status(ErrorCode.COMMON_UNSUPPORTED_MEDIA_TYPE.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_UNSUPPORTED_MEDIA_TYPE.getCode(), ErrorCode.COMMON_UNSUPPORTED_MEDIA_TYPE.getMessage()));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		return ResponseEntity.status(ErrorCode.COMMON_METHOD_NOT_ALLOWED.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_METHOD_NOT_ALLOWED.getCode(), ErrorCode.COMMON_METHOD_NOT_ALLOWED.getMessage()));
	}

	@ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
	public ResponseEntity<ApiResponse<Void>> handleNotFoundException(Exception ex) {
		return ResponseEntity.status(ErrorCode.COMMON_NOT_FOUND.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_NOT_FOUND.getCode(), ErrorCode.COMMON_NOT_FOUND.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
		log.error("Unhandled exception occurred", ex);
		return ResponseEntity.status(ErrorCode.COMMON_INTERNAL_ERROR.getStatus())
				.body(ApiResponse.fail(ErrorCode.COMMON_INTERNAL_ERROR.getCode(), ErrorCode.COMMON_INTERNAL_ERROR.getMessage()));
	}
}
