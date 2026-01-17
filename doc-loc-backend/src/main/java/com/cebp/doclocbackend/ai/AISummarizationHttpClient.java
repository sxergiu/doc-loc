package com.cebp.doclocbackend.ai;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AISummarizationHttpClient implements AISummarizationClient {
    private final RestTemplate restTemplate;
    private final AiClientProperties properties;

    public AISummarizationHttpClient(RestTemplate restTemplate, AiClientProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public SummaryResult summarize(byte[] pdfBytes, String filename, String provider, String model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(properties.getAuthToken());

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("pdf", asResource(pdfBytes, filename));
            body.add("aiService", provider != null ? provider : "openrouter");
            if (model != null && !model.isBlank()) {
                body.add("model", model);
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    properties.getBaseUrl() + "/api/summarize",
                    requestEntity,
                    Map.class);

            Object summaryText = response.getBody() != null ? response.getBody().get("summary") : null;
            Object aiService = response.getBody() != null ? response.getBody().get("aiService") : provider;
            Object modelUsed = response.getBody() != null ? response.getBody().get("model") : model;
            if (summaryText == null) {
                throw new AiClientException("AI service returned no summary text");
            }
            return new SummaryResult(
                    aiService != null ? aiService.toString() : provider,
                    modelUsed != null ? modelUsed.toString() : model,
                    summaryText.toString()
            );
        } catch (Exception ex) {
            throw new AiClientException("Failed to call AI service", ex);
        }
    }

    private ByteArrayResource asResource(byte[] bytes, String filename) {
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename != null ? filename : "document.pdf";
            }
        };
    }
}
