package com.example.demo.dto;

import com.example.demo.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Auth {

	private String email;
	private String password;
	private String role;
	private String token;

	public Auth(User user, String token) {
		email = user.getEmail();
		role = user.getRole();
		this.token = token;
	}

}
