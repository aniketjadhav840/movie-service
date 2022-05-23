package com.gofore.movie.application.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExcpetionHandler extends ResponseEntityExceptionHandler {

	Logger logger = LoggerFactory.getLogger(GlobalExcpetionHandler.class);

	@ExceptionHandler(value = { DataIntegrityViolationException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		logger.trace("Exception occurred while saving movie object Exception: {}",  ex.getCause().toString());
		String bodyOfResponse = "Movie Already Exists";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return handleExceptionInternal(ex, errors, headers, status, request);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	protected ResponseEntity<Object> handleNotExists(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Data Not Found";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleUnknownExcpetions(Exception ex, WebRequest request) {
		logger.trace("Unknown exception occurred while performing operation: {} Exception:", request.getContextPath(),
				ex.getCause().toString());
		String bodyOfResponse = "Please contact system admin.";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}

}
