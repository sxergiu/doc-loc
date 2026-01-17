package com.cebp.doclocbackend.domain.repository;

import com.cebp.doclocbackend.domain.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SummaryRepository extends JpaRepository<Summary, UUID> {
    Optional<Summary> findByDocumentId(UUID documentId);
}
