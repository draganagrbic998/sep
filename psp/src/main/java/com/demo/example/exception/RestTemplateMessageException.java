package com.demo.example.exception;

import com.example.demo.dto.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestTemplateMessageException extends RuntimeException {

	private Error error;

}
