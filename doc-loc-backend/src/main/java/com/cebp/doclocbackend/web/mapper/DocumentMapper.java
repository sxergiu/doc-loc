package com.cebp.doclocbackend.web.mapper;

import com.cebp.doclocbackend.web.dto.DocumentDto;
import com.cebp.doclocbackend.domain.model.Document;
public class DocumentMapper {
    private DocumentMapper() {
    }

    public static DocumentDto toDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setFilename(document.getFilename());
        dto.setContentType(document.getContentType());
        dto.setSizeBytes(document.getSizeBytes());
        dto.setUploadedAt(document.getUploadedAt());
        return dto;
    }
}
