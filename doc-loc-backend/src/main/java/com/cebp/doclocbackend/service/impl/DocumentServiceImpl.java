package com.cebp.doclocbackend.service.impl;

import com.cebp.doclocbackend.web.exception.BadRequestException;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.domain.repository.DocumentRepository;
import com.cebp.doclocbackend.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public UUID saveDocument(MultipartFile file) {
        validateFile(file);
        try {
            Document document = new Document();
            document.setFilename(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setSizeBytes(file.getSize());
            document.setContent(file.getBytes());
            Document saved = documentRepository.save(document);
            return saved.getId();
        } catch (IOException e) {
            throw new BadRequestException("Failed to read uploaded file");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        String type = file.getContentType();
        if (type == null || !type.equalsIgnoreCase("application/pdf")) {
            throw new BadRequestException("Only PDF files are allowed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> get(UUID id) {
        return documentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> listAll() {
        return documentRepository.findAll();
    }
}
