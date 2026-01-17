package com.cebp.doclocbackend.domain.repository;

import com.cebp.doclocbackend.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
}
