package com.cebp.doclocbackend;

import com.cebp.doclocbackend.web.DocumentController;
import com.cebp.doclocbackend.web.dto.DocumentDto;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController controller;

    @Test
    void uploadPdfReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf", "data".getBytes());

        Document doc = new Document();
        doc.setId(id);
        doc.setFilename("doc.pdf");
        doc.setContentType("application/pdf");

        Mockito.when(documentService.saveDocument(any())).thenReturn(id);
        Mockito.when(documentService.get(id)).thenReturn(Optional.of(doc));

        ResponseEntity<DocumentDto> response = controller.upload(file);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getFilename()).isEqualTo("doc.pdf");
    }

    @Test
    void listDocumentsReturnsArray() {
        Document doc = new Document();
        doc.setId(UUID.randomUUID());
        doc.setFilename("doc.pdf");
        doc.setContentType("application/pdf");
        Mockito.when(documentService.listAll()).thenReturn(Collections.singletonList(doc));

        ResponseEntity<List<DocumentDto>> response = controller.list();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getFilename()).isEqualTo("doc.pdf");
    }
}
