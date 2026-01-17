package com.cebp.doclocbackend.web.mapper;

import com.cebp.doclocbackend.web.dto.SummaryResponse;
import com.cebp.doclocbackend.domain.model.Summary;
public class SummaryMapper {
    private SummaryMapper() {
    }

    public static SummaryResponse toResponse(Summary summary) {
        SummaryResponse response = new SummaryResponse();
        response.setSummaryId(summary.getId());
        response.setDocumentId(summary.getDocument() != null ? summary.getDocument().getId() : null);
        response.setAiProvider(summary.getAiProvider());
        response.setAiModel(summary.getAiModel());
        response.setStatus(summary.getStatus());
        response.setSummaryText(summary.getSummaryText());
        response.setCreatedAt(summary.getCreatedAt());
        response.setUpdatedAt(summary.getUpdatedAt());
        response.setFailureReason(summary.getFailureReason());
        return response;
    }
}
