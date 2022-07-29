package com.challenge.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.challenge.api.model.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DocumentDTO {

	private int id;
	private String name;
	private DocumentType type;
	private String path;
	private LocalDateTime uploadTime;
	private String description;
	private UserDTO usernameOwner;
	private List<UserDTO> sharedUsers;
}
