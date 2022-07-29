package com.challenge.api.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.challenge.api.dtos.DocumentDTO;
import com.challenge.api.dtos.DocumentForm;
import com.challenge.api.services.DocumentService;

@RestController
@RequestMapping("/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadNewDocument(DocumentForm documentForm, @RequestParam("document") MultipartFile multipartFile) throws IOException {
		if (documentService.saveNewDocument(documentForm, multipartFile)) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.ok("Error, file already exists or can't upload successfully");
	}

	@GetMapping("/getAllByActualUser")
	public ResponseEntity<List<DocumentDTO>> getAllDocumentsForUser() {
		return ResponseEntity.ok(documentService.getAllDocumentsForUser());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFile(@PathVariable("id") int idFile) {
		if (documentService.deleteFile(idFile)) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.ok("Error, couldn't delete file");
	}

	@PostMapping("/addUsers")
	public ResponseEntity<String> addUsersToFile(@RequestParam("documentId") int documentId, @RequestParam("userIds") List<Integer> usersId) {
		if (documentService.addUsersToDocument(documentId, usersId)) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.ok("Error, maybe someone ids doesn't exists");
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") int id) {
		return ResponseEntity.ok(documentService.getById(id));
	}

	@PostMapping("/edit")
	public ResponseEntity<String> editDocument(DocumentDTO documentDTO) {
		documentService.editDocument(documentDTO);
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> getFile(@PathVariable("id") int idFile)
			throws FileNotFoundException, InvalidAttributeValueException {
		String originalName = documentService.getOriginalName(idFile);
		if (!StringUtils.hasLength(originalName)) {
			return ResponseEntity.notFound().build();
		}
		var file = new File(documentService.getFinalPathOfFile(idFile));
		var resource = new InputStreamResource(new FileInputStream(file));
		var headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalName + "\"");
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
}
