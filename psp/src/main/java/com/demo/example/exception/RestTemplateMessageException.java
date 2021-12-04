package com.demo.example.exception;

import com.example.demo.dto.ErrorDTO;

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

	private ErrorDTO error;

}
