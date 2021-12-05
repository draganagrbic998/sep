package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1871256143128415320L;
	private String id;
	private String className;

	public NotFoundException(String id, String className) {
		super(className + " with id " + id.toString() + " was not found.");
		this.id = id.toString();
		this.className = className;
	}

	public String getErrorMessage() {
		return super.getMessage();
	}

}
