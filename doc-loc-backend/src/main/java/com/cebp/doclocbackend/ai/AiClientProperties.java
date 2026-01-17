package com.cebp.doclocbackend.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai.client")
public class AiClientProperties {
    /**
     * Base URL of the AI summarization service (cebp_ai_service).
     */
    private String baseUrl = "http://localhost:3000";
    /**
     * Bearer token for the AI service.
     */
    private String authToken = "your-secret-token";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
