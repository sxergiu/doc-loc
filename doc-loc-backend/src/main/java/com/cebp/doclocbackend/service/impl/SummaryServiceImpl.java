package com.cebp.doclocbackend.service.impl;

import com.cebp.doclocbackend.ai.AISummarizationClient;
import com.cebp.doclocbackend.ai.AiClientException;
import com.cebp.doclocbackend.ai.SummaryResult;
import com.cebp.doclocbackend.web.exception.NotFoundException;
import com.cebp.doclocbackend.domain.model.Document;
import com.cebp.doclocbackend.domain.model.Summary;
import com.cebp.doclocbackend.domain.model.SummaryStatus;
import com.cebp.doclocbackend.domain.repository.DocumentRepository;
import com.cebp.doclocbackend.domain.repository.SummaryRepository;
import com.cebp.doclocbackend.service.SummaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SummaryServiceImpl implements SummaryService {
    private final DocumentRepository documentRepository;
    private final SummaryRepository summaryRepository;
    private final AISummarizationClient aiClient;

    public SummaryServiceImpl(DocumentRepository documentRepository,
                              SummaryRepository summaryRepository,
                              AISummarizationClient aiClient) {
        this.documentRepository = documentRepository;
        this.summaryRepository = summaryRepository;
        this.aiClient = aiClient;
    }

    @Override
    @Transactional
    public Summary createFromDocument(UUID docId, String provider, String model) {
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new NotFoundException("Document not found"));

        Summary summary = new Summary();
        summary.setDocument(document);
        summary.setAiProvider(provider != null ? provider : "openrouter");
        summary.setAiModel(model);
        summary.setStatus(SummaryStatus.PENDING);
        summary.setCreatedAt(Instant.now());
        summary.setUpdatedAt(summary.getCreatedAt());
        summaryRepository.save(summary);

        try {
            SummaryResult result = aiClient.summarize(document.getContent(), document.getFilename(), provider, model);
            summary.setSummaryText(result.getSummaryText());
            summary.setAiProvider(result.getProvider() != null ? result.getProvider() : provider);
            summary.setAiModel(result.getModel() != null ? result.getModel() : model);
            summary.setStatus(SummaryStatus.READY);
        } catch (AiClientException ex) {
            summary.setStatus(SummaryStatus.FAILED);
            summary.setFailureReason(ex.getMessage());
        }
        summary.setUpdatedAt(Instant.now());
        return summaryRepository.save(summary);
    }

    @Override
    @Transactional(readOnly = true)
    public Summary get(UUID summaryId) {
        return summaryRepository.findById(summaryId)
                .orElseThrow(() -> new NotFoundException("Summary not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Summary> listAll() {
        return summaryRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(UUID summaryId) {
        if (!summaryRepository.existsById(summaryId)) {
            throw new NotFoundException("Summary not found");
        }
        summaryRepository.deleteById(summaryId);
    }
}
