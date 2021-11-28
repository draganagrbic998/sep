package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Role;
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
	private List<String> roles;
	private String token;

	public Auth(User user, String token) {
		email = user.getEmail();
		roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
		this.token = token;
	}

}
