package com.challenge.api.services;

import java.io.IOException;
import java.security.AccessControlException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.api.dtos.DocumentDTO;
import com.challenge.api.dtos.DocumentForm;
import com.challenge.api.mappers.DocumentMapper;
import com.challenge.api.model.Document;
import com.challenge.api.model.User;
import com.challenge.api.repositories.DocumentRepository;
import com.challenge.api.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentService {

	@Value("${custom.doc-folder}")
	private String docRootPath;

	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private DocumentMapper documentMapper;
	@Autowired
	private FileUtils fileUtils;

	public boolean saveNewDocument(DocumentForm documentForm, MultipartFile file) {
		User owner = userService.getLoggedUser();
		String originalFileName = file.getOriginalFilename();
		String uniqueFileNameForUser = owner.getUsername() + "_-_" + originalFileName;
		if (documentRepository.findByNameAndOwner(originalFileName, owner).isPresent()) {
			log.warn("File {} already exists for user {}", originalFileName, owner.getUsername());
			return false;
		}
		try {
			fileUtils.saveFile(docRootPath, uniqueFileNameForUser, file);
		} catch (IOException ex) {
			log.error("Error at upload file {}", originalFileName);
			return false;
		}
		var document = new Document(file.getOriginalFilename(), documentForm.getType(), LocalDateTime.now(),
				documentForm.getDescription(), uniqueFileNameForUser, owner);
		documentRepository.save(document);
		log.info("Successfully upload file {} for user {}", originalFileName, owner.getUsername());
		return true;
	}

	public List<DocumentDTO> getAllDocumentsForUser() {
		User loggedUser = userService.getLoggedUser();
		return documentMapper.entitiesToDocumentDTOs(documentRepository.getAllByOwnerOrUserGroup(loggedUser, loggedUser));
	}

	public boolean deleteFile(int id) {
		Optional<Document> document = documentRepository.findById(id);
		if (document.isEmpty() || !fileUtils.deleteFile(docRootPath, document.get().getPath())) {
			return false;
		}
		documentRepository.delete(document.get());
		return true;
	}

	public boolean addUsersToDocument(int documentId, List<Integer> usersIds) {
		List<Optional<User>> users = usersIds.stream().map(userService::findUserById).collect(Collectors.toList());
		Optional<Document> document = documentRepository.findById(documentId);
		if (users.stream().anyMatch(Optional::isEmpty)) {
			log.warn("An invalid user id was entered");
			return false;
		}
		if (document.isEmpty()) {
			log.warn("An invalid document id was entered {}", documentId);
			return false;
		}
		Document file = document.get();
		file.setUserGroup(users.stream().map(user -> user.get()).collect(Collectors.toSet()));
		return true;
	}

	public DocumentDTO getById(int id) {
		return documentMapper.entityToDocumentDTO(documentRepository.findById(id).orElseGet(null));
	}

	public void editDocument(DocumentDTO documentDTO) {
		Optional<Document> document = documentRepository.findById(documentDTO.getId());
		User loggedUser = userService.getLoggedUser();
		if(document.isPresent() && document.get().getOwner().getId() != loggedUser.getId()) {
			throw new AccessControlException("User isn't owner of this file");
		}
		document.ifPresent(doc -> {
			doc.setDescription(documentDTO.getDescription());
			doc.setDocumentType(documentDTO.getType());
			doc.setUserGroup(documentDTO.getSharedUsers().stream().map(user -> userService.getById(user.getId())).collect(Collectors.toSet()));
		});
	}

	public String getFinalPathOfFile(int documentId) throws InvalidAttributeValueException {
		Optional<Document> document = documentRepository.findById(documentId);
		if (document.isPresent()) {
			return docRootPath + document.get().getPath();
		}
		throw new InvalidAttributeValueException("Not found document path for id");
	}

	public String getOriginalName(int documentId) throws InvalidAttributeValueException {
		Optional<Document> document = documentRepository.findById(documentId);
		if (document.isPresent()) {
			return document.get().getName();
		}
		throw new InvalidAttributeValueException("Not found document path for id");
	}
}
