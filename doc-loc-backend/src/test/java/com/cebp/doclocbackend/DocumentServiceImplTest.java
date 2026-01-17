package com.cebp.doclocbackend;

import com.cebp.doclocbackend.web.exception.BadRequestException;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.domain.repository.DocumentRepository;
import com.cebp.doclocbackend.service.impl.DocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    void saveDocument_rejectsNonPdf() {
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");

        assertThatThrownBy(() -> documentService.saveDocument(file))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Only PDF");
    }

    @Test
    void saveDocument_storesMetadata() throws Exception {
        UUID id = UUID.randomUUID();
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getOriginalFilename()).thenReturn("doc.pdf");
        when(file.getSize()).thenReturn(123L);
        when(file.getBytes()).thenReturn("hello".getBytes());

        Document saved = new Document();
        saved.setId(id);
        saved.setFilename("doc.pdf");
        when(documentRepository.save(any(Document.class))).thenReturn(saved);

        UUID result = documentService.saveDocument(file);
        assertThat(result).isEqualTo(id);
    }
}
