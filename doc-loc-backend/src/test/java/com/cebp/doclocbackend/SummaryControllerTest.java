package com.cebp.doclocbackend;

import com.cebp.doclocbackend.web.SummaryController;
import com.cebp.doclocbackend.web.dto.SummaryRequest;
import com.cebp.doclocbackend.web.dto.SummaryResponse;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.domain.model.Summary;
import com.cebp.doclocbackend.domain.model.SummaryStatus;
import com.cebp.doclocbackend.service.SummaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SummaryControllerTest {

    @Mock
    private SummaryService summaryService;

    @InjectMocks
    private SummaryController controller;

    @Test
    void createSummaryReturnsCreated() {
        UUID docId = UUID.randomUUID();
        UUID sumId = UUID.randomUUID();
        Summary summary = new Summary();
        summary.setId(sumId);
        Document doc = new Document();
        doc.setId(docId);
        summary.setDocument(doc);
        summary.setStatus(SummaryStatus.READY);
        summary.setSummaryText("hello");
        Mockito.when(summaryService.createFromDocument(any(), any(), any())).thenReturn(summary);

        SummaryRequest req = new SummaryRequest();
        req.setDocumentId(docId);
        req.setAiProvider("openrouter");

        ResponseEntity<SummaryResponse> response = controller.create(req);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSummaryId()).isEqualTo(sumId);
        assertThat(response.getBody().getStatus()).isEqualTo(SummaryStatus.READY);
    }

    @Test
    void getSummary() {
        UUID sumId = UUID.randomUUID();
        Summary summary = new Summary();
        summary.setId(sumId);
        summary.setStatus(SummaryStatus.READY);
        Mockito.when(summaryService.get(sumId)).thenReturn(summary);

        ResponseEntity<SummaryResponse> response = controller.get(sumId);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSummaryId()).isEqualTo(sumId);
    }

    @Test
    void listSummaries() {
        Summary summary = new Summary();
        summary.setId(UUID.randomUUID());
        summary.setStatus(SummaryStatus.READY);
        Mockito.when(summaryService.listAll()).thenReturn(Collections.singletonList(summary));

        ResponseEntity<List<SummaryResponse>> response = controller.list();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getSummaryId()).isEqualTo(summary.getId());
    }

    @Test
    void deleteSummaryReturnsNoContent() {
        UUID sumId = UUID.randomUUID();
        ResponseEntity<Void> response = controller.delete(sumId);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
