package com.cebp.doclocbackend.web;

import com.cebp.doclocbackend.web.dto.DocumentDto;
import com.cebp.doclocbackend.web.exception.NotFoundException;
import com.cebp.doclocbackend.web.mapper.DocumentMapper;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<DocumentDto> upload(@Valid @org.springframework.web.bind.annotation.RequestParam("file") MultipartFile file) {
        UUID id = documentService.saveDocument(file);
        DocumentDto dto = documentService.get(id)
                .map(DocumentMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Document not found after save"));
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DocumentDto>> list() {
        List<DocumentDto> docs = documentService.listAll().stream()
                .map(DocumentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(docs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> get(@PathVariable UUID id) {
        Document doc = documentService.get(id)
                .orElseThrow(() -> new NotFoundException("Document not found"));
        return ResponseEntity.ok(DocumentMapper.toDto(doc));
    }
}
