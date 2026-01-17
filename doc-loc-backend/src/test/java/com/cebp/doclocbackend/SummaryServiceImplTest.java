package com.cebp.doclocbackend;

import com.cebp.doclocbackend.ai.AISummarizationClient;
import com.cebp.doclocbackend.ai.AiClientException;
import com.cebp.doclocbackend.ai.SummaryResult;
import com.cebp.doclocbackend.web.exception.NotFoundException;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.domain.model.Summary;
import com.cebp.doclocbackend.domain.model.SummaryStatus;
import com.cebp.doclocbackend.domain.repository.DocumentRepository;
import com.cebp.doclocbackend.domain.repository.SummaryRepository;
import com.cebp.doclocbackend.service.impl.SummaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SummaryServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private SummaryRepository summaryRepository;
    @Mock
    private AISummarizationClient aiClient;

    @InjectMocks
    private SummaryServiceImpl summaryService;

    @Test
    void createFromDocument_throwsWhenDocMissing() {
        UUID docId = UUID.randomUUID();
        when(documentRepository.findById(docId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> summaryService.createFromDocument(docId, "openrouter", null))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createFromDocument_setsReadyOnSuccess() {
        UUID docId = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(docId);
        doc.setFilename("doc.pdf");
        doc.setContentType("application/pdf");
        doc.setContent("hello".getBytes());
        when(documentRepository.findById(docId)).thenReturn(Optional.of(doc));

        when(summaryRepository.save(any(Summary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(aiClient.summarize(any(), any(), any(), any()))
                .thenReturn(new SummaryResult("openrouter", "model-a", "summary text"));

        Summary result = summaryService.createFromDocument(docId, "openrouter", "model-a");

        assertThat(result.getStatus()).isEqualTo(SummaryStatus.READY);
        assertThat(result.getSummaryText()).isEqualTo("summary text");
    }

    @Test
    void createFromDocument_setsFailedOnError() {
        UUID docId = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(docId);
        doc.setFilename("doc.pdf");
        doc.setContentType("application/pdf");
        doc.setContent("hello".getBytes());
        when(documentRepository.findById(docId)).thenReturn(Optional.of(doc));

        when(summaryRepository.save(any(Summary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(aiClient.summarize(any(), any(), any(), any()))
                .thenThrow(new AiClientException("fail"));

        Summary result = summaryService.createFromDocument(docId, "openrouter", "model-a");

        assertThat(result.getStatus()).isEqualTo(SummaryStatus.FAILED);
        assertThat(result.getFailureReason()).contains("fail");
    }
}
