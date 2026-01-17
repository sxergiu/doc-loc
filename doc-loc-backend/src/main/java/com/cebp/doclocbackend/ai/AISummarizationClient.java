package com.cebp.doclocbackend.ai;

public interface AISummarizationClient {
    SummaryResult summarize(byte[] pdfBytes, String filename, String provider, String model);
}
