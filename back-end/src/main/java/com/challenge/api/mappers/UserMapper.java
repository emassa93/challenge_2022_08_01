package com.challenge.api.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.challenge.api.dtos.UserDTO;
import com.challenge.api.model.User;

@Component
public class UserMapper {

	public UserDTO entityToUserDTO(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getSurname());
	}

	public List<UserDTO> entitiesToUserDTOs(Collection<User> users) {
		return users.stream().map(this::entityToUserDTO).collect(Collectors.toList());
	}

}
