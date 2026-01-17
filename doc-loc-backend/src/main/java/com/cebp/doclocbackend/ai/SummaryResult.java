package com.cebp.doclocbackend.ai;

public class SummaryResult {
    private final String provider;
    private final String model;
    private final String summaryText;

    public SummaryResult(String provider, String model, String summaryText) {
        this.provider = provider;
        this.model = model;
        this.summaryText = summaryText;
    }

    public String getProvider() {
        return provider;
    }

    public String getModel() {
        return model;
    }

    public String getSummaryText() {
        return summaryText;
    }
}
