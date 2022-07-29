package com.challenge.api.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

	private static final long serialVersionUID = -88561693616367371L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String username;

	@Column
	private String password;

	@Column
	private String name;

	@Column
	private String surname;

	@OneToMany(mappedBy = "owner")
	private Set<Document> documents;

	@ManyToMany(mappedBy = "userGroup", fetch = FetchType.EAGER)
	private Set<Document> sharedDocuments;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		var authority = new SimpleGrantedAuthority("user");
		return Collections.singleton(authority);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public User() {

	}

	public User(String email, String username, String password, String name, String surname) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
	}
}
