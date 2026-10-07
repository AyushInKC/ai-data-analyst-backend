package com.ayush_singh.ai_data_analyst.Service;

import com.ayush_singh.ai_data_analyst.Model.ChatResponse;
import com.cohere.api.Cohere;
import com.cohere.api.resources.v2.requests.V2ChatRequest;
import com.cohere.api.types.ChatMessageV2;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    private final Cohere cohere;
    private final String model;

    public AiService(
            @Value("${cohere.api-key}") String apiKey,
            @Value("${cohere.model}") String model
    ) {
        this.cohere = Cohere.builder()
                .token(apiKey)
                .clientName("ai-data-analyst")
                .build();

        this.model = model;
    }

    private String askCohere(String prompt) {

        ChatResponse response = cohere
                .v2()
                .chat(
                        V2ChatRequest.builder()
                                .model(model)
                                .messages(
                                        List.of(
                                                ChatMessageV2.user(
                                                        UserMessage.builder()
                                                                .content(
                                                                        UserMessageContent.of(prompt)
                                                                )
                                                                .build()
                                                )
                                        )
                                )
                                .build()
                );

        return response
                .message()
                .content()
                .get(0)
                .text();
    }

    public String generateSql(
            String tableName,
            String question
    ) {

        String prompt = """
                You are a SQL generator for a data analysis application.

                You are given a DuckDB table.

                Table name:
                %s

                User question:
                %s

                Generate ONLY a single SQL SELECT query.

                Rules:
                - Use DuckDB SQL.
                - Only generate SELECT or WITH queries.
                - Do not use INSERT, UPDATE, DELETE, DROP, ALTER or CREATE.
                - Use only the provided table.
                - Do not explain the SQL.
                - Return only SQL.
                """.formatted(tableName, question);

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
                """.formatted(
                question,
                sql,
                result
        );

        return askCohere(prompt);
    }
}