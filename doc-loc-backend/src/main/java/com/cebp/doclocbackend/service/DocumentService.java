package com.cebp.doclocbackend.service;

import com.cebp.doclocbackend.domain.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {
    UUID saveDocument(MultipartFile file);
    Optional<Document> get(UUID id);
    List<Document> listAll();
}
