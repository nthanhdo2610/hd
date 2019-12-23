package com.tinhvan.hd.filehandler.controller;

import com.tinhvan.hd.filehandler.exception.*;
import com.tinhvan.hd.filehandler.payload.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class BaseController {

	public ResponseEntity<?> json(ResponseDTO<?> response, int statusCode) {
		BodyBuilder builder = ResponseEntity.status(statusCode);
		return builder.body(response);
	}

	public ResponseEntity<?> json(ResponseDTO<?> response) {
		return json(response, response.getCode());
	}

	public ResponseEntity<?> json(ResponseDTO<?> response, HttpStatus statusCode) {
		return json(response, statusCode.value());
	}

	public ResponseEntity<?> ok(Object payload) {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.OK.value(), payload);
		return json(dto);
	}

	public ResponseEntity<?> ok() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.OK.value());
		return json(dto);
	}

	public ResponseEntity<?> ok(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.OK);
	}

	public ResponseEntity<?> ok(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.OK);
	}

	public ResponseEntity<?> badRequest(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> badRequest(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> badRequest() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value());
		return json(dto, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> unauthorized(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> unauthorized() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.UNAUTHORIZED.value());
		return json(dto, HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> unauthorized(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.UNAUTHORIZED);
	}

	public ResponseEntity<?> forbidden(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> forbidden(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.FORBIDDEN);
	}

	public ResponseEntity<?> forbidden() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.FORBIDDEN.value());
		return json(dto);
	}

	public ResponseEntity<?> notFound(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<?> notFound(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<?> notFound() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.NOT_FOUND.value());
		return json(dto);
	}

	public ResponseEntity<?> serverError(int code, String msg) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> serverError(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> serverError() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return json(dto);
	}

	public ResponseEntity<?> conflict() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.CONFLICT.value());
		return json(dto);
	}

	public ResponseEntity<?> conflict(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.CONFLICT);
	}

	public ResponseEntity<?> notAcceptable(int code) {
		ResponseDTO<Object> dto = new ResponseDTO<>(code);
		return json(dto, HttpStatus.NOT_ACCEPTABLE);
	}

	public ResponseEntity<?> notAcceptable() {
		ResponseDTO<Object> dto = new ResponseDTO<>(HttpStatus.NOT_ACCEPTABLE.value());
		return json(dto);
	}

	@ExceptionHandler(HandlerException.class)
    public ResponseEntity<?> handleException(HandlerException e) {
		if (e instanceof BadRequestException) {
			return badRequest(e.getCode(), e.getMessage());
		}
		if (e instanceof ForbiddenException) {
			return forbidden(e.getCode(), e.getMessage());
		}
		if (e instanceof UnauthorizedException) {
			return unauthorized(e.getCode(), e.getMessage());
		}
		if (e instanceof NotFoundException) {
			return notFound(e.getCode(), e.getMessage());
		}
		if (e instanceof ConflictException) {
			return conflict(e.getCode());
		}
		if (e instanceof NotAcceptableException) {
			return notAcceptable(e.getCode());
		}
		return serverError(e.getCode(), e.getMessage());
	}

	protected void finalize() {}

}
