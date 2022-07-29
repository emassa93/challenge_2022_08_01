package com.challenge.api.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;

	@Column(name = "upload_date")
	private LocalDateTime uploadDate;

	@Column
	private String description;

	@Column
	private String path;

	@ManyToOne
	@JoinColumn(name = "owner", nullable = false)
	private User owner;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "document_has_users", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> userGroup;

	public Document() {

	}

	public Document(String name, DocumentType documentType, LocalDateTime uploadDate, String description, String path, User owner) {
		this.name = name;
		this.documentType = documentType;
		this.uploadDate = uploadDate;
		this.description = description;
		this.path = path;
		this.owner = owner;
	}

}
