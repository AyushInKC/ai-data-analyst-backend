package com.ayush_singh.ai_data_analyst.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public AiService(
            @Value("${cohere.api-key}") String apiKey,
            @Value("${cohere.model}") String model
    ) {
        this.objectMapper = new ObjectMapper();
        this.model = model;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.cohere.com")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    private String askCohere(String prompt) {

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        try {
            String response = restClient
                    .post()
                    .uri("/v2/chat")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(response);

            JsonNode content = root
                    .path("message")
                    .path("content");

            if (!content.isArray() || content.size() == 0) {
                throw new RuntimeException(
                        "Cohere returned an unexpected response: " + response
                );
            }

            for (JsonNode item : content) {
                if ("text".equals(item.path("type").asText())) {
                    return item.path("text").asText();
                }
            }

            throw new RuntimeException(
                    "No text content found in Cohere response"
            );

        } catch (RestClientResponseException e) {
            throw new RuntimeException(
                    "Cohere API error: " + e.getResponseBodyAsString(),
                    e
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to communicate with Cohere",
                    e
            );
        }
    }

    public String generateSql(
            String tableName,
            List<String> columns,
            String question
    ) {

        String schema = String.join(", ", columns);

        String prompt = """
            You are a SQL generator for a data analysis application.

            You are given a DuckDB table.

            Table name:
            %s

            Available columns:
            %s

            User question:
            %s

            Generate ONLY a single SQL query.

            Rules:
            - Use DuckDB SQL.
            - Only generate SELECT or WITH queries.
            - Do not use INSERT, UPDATE, DELETE, DROP, ALTER or CREATE.
            - Use only the provided table.
            - Use only columns that actually exist in the provided schema.
            - Never invent column names.
            - If the question cannot be answered using the available columns, return:
              SELECT 'INSUFFICIENT_DATA' AS error;
            - Do not explain the SQL.
            - Return only SQL.
            """.formatted(
                tableName,
                schema,
                question
        );

        return askCohere(prompt);
    }
    public String generateAnswer(
            String question,
            String sql,
            Object result
    ) {

        String prompt = """
                You are an AI data analyst.

                User question:
                %s

                SQL executed:
                %s

                SQL result:
                %s

                Answer the user's question using ONLY the provided result.

                Rules:
                - Do not invent numbers.
                - Be concise.
                - Clearly state the result.
                - If the result is empty, say that no matching data was found.
                """.formatted(question, sql, result);

        return askCohere(prompt);
    }
}