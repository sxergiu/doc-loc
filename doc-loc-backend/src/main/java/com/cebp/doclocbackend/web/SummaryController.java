package com.cebp.doclocbackend.web;

import com.cebp.doclocbackend.web.dto.SummaryRequest;
import com.cebp.doclocbackend.web.dto.SummaryResponse;
import com.cebp.doclocbackend.web.exception.NotFoundException;
import com.cebp.doclocbackend.web.mapper.SummaryMapper;
import com.cebp.doclocbackend.domain.model.Summary;
import com.cebp.doclocbackend.service.SummaryService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/summaries")
public class SummaryController {
    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping
    public ResponseEntity<SummaryResponse> create(@Valid @RequestBody SummaryRequest request) {
        Summary summary = summaryService.createFromDocument(
                request.getDocumentId(),
                request.getAiProvider(),
                request.getAiModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(SummaryMapper.toResponse(summary));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SummaryResponse> get(@PathVariable UUID id) {
        Summary summary = summaryService.get(id);
        return ResponseEntity.ok(SummaryMapper.toResponse(summary));
    }

    @GetMapping
    public ResponseEntity<List<SummaryResponse>> list() {
        List<SummaryResponse> responses = summaryService.listAll().stream()
                .map(SummaryMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID id) {
        Summary summary = summaryService.get(id);
        if (summary.getSummaryText() == null) {
            throw new NotFoundException("Summary text not available");
        }
        byte[] bytes = summary.getSummaryText().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("summary-" + id + ".txt")
                .build());
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        summaryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
