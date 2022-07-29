package com.challenge.api.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.api.dtos.UserDTO;
import com.challenge.api.dtos.UserForm;
import com.challenge.api.mappers.UserMapper;
import com.challenge.api.model.User;
import com.challenge.api.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AppUserDetailsService appUserDetailsService;
	@Autowired
	private UserMapper userMapper;

	public Optional<User> findUserById(int id) {
		return userRepository.findById(id);
	}

	public User getById(int id) {
		return this.findUserById(id).orElseThrow();
	}

	public boolean signUpUser(UserForm userDTO) {
		if (userRepository.findByUsername(userDTO.getUserName()).isEmpty()) {
			var user = new User(userDTO.getEmail(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getName(), userDTO.getSurname());
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}

	public Map<String, String> getProfile() {
		return appUserDetailsService.getUsernameLogged().map(user -> {
			return userRepository.findByUsername(user).map(userInDb -> {
				return Map.of("name", userInDb.getName(), "surname", userInDb.getSurname(), "username", userInDb.getUsername(), "email", userInDb.getEmail());
			}).orElseThrow();
		}).orElseThrow();
	}

	public User getLoggedUser() {
		String userName = appUserDetailsService.getUsernameLogged().orElseThrow();
		return userRepository.findByUsername(userName).orElseThrow();
	}

	public List<UserDTO> getUsers() {
		List<User> users = userRepository.findAll();
		users.remove(this.getLoggedUser());
		return userMapper.entitiesToUserDTOs(users);
	}
}
