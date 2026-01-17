package com.cebp.doclocbackend.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class SummaryRequest {
    @NotNull
    private UUID documentId;
    private String aiProvider;
    private String aiModel;

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getAiProvider() {
        return aiProvider;
    }

    public void setAiProvider(String aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String getAiModel() {
        return aiModel;
    }

    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }
}
