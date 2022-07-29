package com.challenge.api.dtos;

import com.challenge.api.model.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DocumentForm {

	private String description;
	private DocumentType type;
}
