package com.challenge.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.api.dtos.UserDTO;
import com.challenge.api.dtos.UserForm;
import com.challenge.api.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUpUser(UserForm userDTO) {
		if (userService.signUpUser(userDTO)) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.ok("User already exists");
	}

	@GetMapping("/list")
	public ResponseEntity<List<UserDTO>> listUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}
}
