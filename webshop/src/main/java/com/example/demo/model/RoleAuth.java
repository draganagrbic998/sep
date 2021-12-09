package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RoleAuth implements GrantedAuthority {

	private String name;

	@Override
	public String getAuthority() {
		return name;
	}

}
