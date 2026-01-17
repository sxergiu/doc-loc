package com.cebp.doclocbackend.service;

import com.cebp.doclocbackend.domain.model.Summary;

import java.util.List;
import java.util.UUID;

public interface SummaryService {
    Summary createFromDocument(UUID docId, String provider, String model);
    Summary get(UUID summaryId);
    List<Summary> listAll();
    void delete(UUID summaryId);
}
