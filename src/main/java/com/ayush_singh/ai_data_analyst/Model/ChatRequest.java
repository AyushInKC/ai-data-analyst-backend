package com.ayush_singh.ai_data_analyst.Model;

public record ChatRequest(
        String sessionId,

        String datasetId,
        String question
) {
}
