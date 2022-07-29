package com.challenge.api.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.challenge.api.dtos.DocumentDTO;
import com.challenge.api.model.Document;

@Component
public class DocumentMapper {

	@Autowired
	private UserMapper userMapper;

	public DocumentDTO entityToDocumentDTO(Document document) {
		return new DocumentDTO(document.getId(), document.getName(), document.getDocumentType(), document.getPath(),
				document.getUploadDate(), document.getDescription(), userMapper.entityToUserDTO(document.getOwner()),
				userMapper.entitiesToUserDTOs(document.getUserGroup()));
	}

	public List<DocumentDTO> entitiesToDocumentDTOs(List<Document> documents) {
		return documents.stream().map(this::entityToDocumentDTO).collect(Collectors.toList());
	}
}
