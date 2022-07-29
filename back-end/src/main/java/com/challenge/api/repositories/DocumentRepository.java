package com.challenge.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.api.model.Document;
import com.challenge.api.model.User;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

	Optional<Document> findByNameAndOwner(String name, User owner);

	List<Document> getAllByOwnerOrUserGroup(User owner, User sharedOwner);

}
