package com.challenge.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserForm {

	private String email;
	private String userName;
	private String password;
	private String name;
	private String surname;

}
