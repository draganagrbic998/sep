package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Auth {

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Valid email is required")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	private String password;

	private String role;
	private String token;

	public Auth(User user, String token) {
		email = user.getEmail();
		role = user.getRole();
		this.token = token;
	}

}
